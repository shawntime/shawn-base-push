package com.shawntime.base.push.service.push;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.shawntime.base.push.dao.BasePushRecordMapper;
import com.shawntime.base.push.dao.BasePushRequestLogMapper;
import com.shawntime.base.push.entity.BasePushRecord;
import com.shawntime.base.push.entity.BasePushRequestLog;
import com.shawntime.base.push.enums.BasePushStatusEnum;
import com.shawntime.base.push.entity.push.BasePushBody;
import com.shawntime.base.push.entity.push.BasePushProtocol;
import com.shawntime.base.push.entity.push.PushHandleContext;
import com.shawntime.base.push.enums.BasePushBusinessEnum;
import com.shawntime.base.push.enums.BasePushLogStatusEnum;
import com.shawntime.base.push.enums.OperationTypeEnum;
import com.shawntime.base.push.enums.PushOperationTypeEnum;
import com.shawntime.base.push.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @title: 基础推送服务推送抽象类
 * @description: 基础推送服务推送抽象类
 * @author mashaohua
 * @date 2020/09/24 13:09
 */
@Slf4j
public abstract class AbstractBasePushService implements IBasePushService, IPushHandleService {

    @Resource
    private BasePushRecordMapper basePushRecordMapper;

    @Resource
    private BasePushRequestLogMapper basePushRequestLogMapper;

    @Resource
    private ThreadPoolTaskExecutor basePushTaskExecutor;

    /**
     * 1 查询所有报名中推送记录列表A
     * 2 查询推送中或暂停中记录列表B
     * 3 A和B数据做对比
     *    3.1 A中存在，B中不存在：推上线
     *    3.2 A中不存在，B中存在：推下线
     *    3.3 A中存在，B中也存在
     *        3.3.1 A中的编辑时间大于B中的编辑时间：推修改
     *        3.3.2 不处理
     * 4 推送记录入库
     */
    @Override
    public void push(PushMode mode) {
        List<BasePushProtocol> basePushProtocolList = getBasePushProtocolList();
        Map<String, BasePushRecord> onlineBasePushRecordMap = getOnlineBasePushRecordMap();
        List<BasePushBody> basePushBodies = handle(mode, basePushProtocolList, onlineBasePushRecordMap);
        if (CollectionUtils.isEmpty(basePushBodies)) {
            return;
        }
        int basePushSize = basePushBodies.size();
        long timeStamp = Instant.now().toEpochMilli();
        long prePushCount = preHandlePushCount();
        executeHandle(mode, basePushBodies, basePushSize);
        long time = (Instant.now().toEpochMilli() - timeStamp) / 1000;
        log.info("businessId:{}, typeId:{}推送数量:{}, 耗时:{}秒",
                getBasePushBusinessEnum().getCode(),
                getSubBusinessId(),
                basePushSize, time);
        long postPushCount = postHandlePushCount();
        comparePushCount(prePushCount, postPushCount);
    }

    /**
     * 业务线
     */
    public abstract BasePushBusinessEnum getBasePushBusinessEnum();

    /**
     * 获取推送唯一标识
     */
    protected abstract String getUniqueId(BasePushProtocol basePushProtocol);

    /**
     * 推上线或者或推修改
     * 推送数据唯一id标识，用于推送修改时找到指定记录：
     *      1）调用方生成维护
     *      2）第一次生产成功后，被调用方返回。returnUniqueId用于保存接口返回的唯一id
     * 如果有回调，则有回调唯一标识
     *      1）调用方生成维护
     *      2）被调用方返回
     *
     */

    /**
     * 子业务线
     */
    public abstract int getSubBusinessId();

    /**
     * 是否需要时间戳限制
     */
    protected boolean isNeedDateTimestamp() {
        return false;
    }

    protected BasePushStatusEnum getEndBasePushStatusEnum() {
        return BasePushStatusEnum.PUSH_END;
    }

    protected boolean hasCallBack() {
        return false;
    }

    /**
     * 前置查询应该推送条数
     */
    protected long preHandlePushCount() {
        return 0L;
    }

    /**
     * 后置查询实际推送条数
     */
    protected long postHandlePushCount() {
        return 0L;
    }

    protected <T> T convert(BasePushProtocol pushProtocol, Class<T> clazz) {
        return (T) pushProtocol;
    }


    private void executeHandle(PushMode mode,
                                List<BasePushBody> basePushBodies,
                                int basePushSize) {
        boolean allowCurrency = mode.hasMode(PushMode.ALLOW_CURRENCY);
        boolean allowLogToDB = mode.hasMode(PushMode.ALLOW_LOG_TO_DB);
        if (allowCurrency) {
            CountDownLatch countDownLatch = new CountDownLatch(basePushSize);
            basePushBodies.forEach(basePushBody ->
                    basePushTaskExecutor.execute(() -> handleBody(basePushBody, countDownLatch, allowLogToDB)));
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                log.error("基础推送:businessId:{}, subBusinessId:{}, countDownLatch中断",
                        getBasePushBusinessEnum().getCode(),
                        getSubBusinessId(), e);
            }
        } else {
            basePushBodies.forEach(basePushBody -> handleBody(basePushBody, null, allowLogToDB));
        }
    }

    private Map<String, BasePushRecord> getOnlineBasePushRecordMap() {
        BasePushBusinessEnum businessEnum = getBasePushBusinessEnum();
        List<BasePushRecord> pushRecordList =
                basePushRecordMapper.getOnlineBasePushRecordList(businessEnum.getCode(), getSubBusinessId(), isNeedDateTimestamp());
        return pushRecordList.stream()
                .collect(Collectors.toMap(BasePushRecord::getUniqueId, t -> t, (o, n) -> n));
    }

    private void handleBody(BasePushBody basePushBody,
                            CountDownLatch countDownLatch,
                            boolean isLogToDB) {
        try {
            PushOperationTypeEnum operationEnum = basePushBody.getPushOperationTypeEnum();
            if (operationEnum == PushOperationTypeEnum.ADD) {
                online(basePushBody.getBasePushProtocol(), isLogToDB);
            }
            if (operationEnum == PushOperationTypeEnum.EDIT) {
                edit(basePushBody.getBasePushProtocol(),
                        basePushBody.getBasePushRecord(),
                        isLogToDB);
            }
            if (operationEnum == PushOperationTypeEnum.DOWN_LINE) {
                downLine(basePushBody.getBasePushRecord(), isLogToDB);
            }
        } catch (Exception e) {
            log.error("businessId:{}, typeId:{}推送失败，basePushBody:[{}]",
                    getBasePushBusinessEnum().getCode(),
                    getSubBusinessId(),
                    JsonHelper.serialize(basePushBody), e);
        } finally {
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }

    private List<BasePushBody> handle(PushMode mode,
                                       List<BasePushProtocol> basePushProtocolList,
                                       Map<String, BasePushRecord> onlineBasePushRecordMap) {
        boolean allowAllDownLine = mode.hasMode(PushMode.ALLOW_ALL_DOWN_LINE);
        boolean allowAllUpdate = mode.hasMode(PushMode.ALLOW_ALL_UPDATE);
        List<BasePushBody> basePushBodies = new ArrayList<>();
        if (!allowAllDownLine) {
            for (BasePushProtocol basePushProtocol : basePushProtocolList) {
                String uniqueId = getUniqueId(basePushProtocol);
                BasePushRecord basePushRecord = onlineBasePushRecordMap.get(uniqueId);
                if (basePushRecord == null) {
                    // 上线
                    BasePushBody basePushBody = BasePushBody.builder()
                            .basePushProtocol(basePushProtocol)
                            .pushOperationTypeEnum(PushOperationTypeEnum.ADD)
                            .build();
                    basePushBodies.add(basePushBody);
                } else {
                    onlineBasePushRecordMap.remove(uniqueId);
                    Date editTime = basePushProtocol.getEditTime();
                    if (editTime == null) {
                        log.error("businessId:{}，typeId:{}编辑时间为空，data:{}",
                                getBasePushBusinessEnum().getCode(),
                                getSubBusinessId(),
                                JsonHelper.serialize(basePushProtocol));
                        continue;
                    }
                    if (allowAllUpdate || editTime.after(basePushRecord.getModifiedStime())
                            || BasePushStatusEnum.PUSH_ING.getCode() == basePushRecord.getPushStatus()) {
                        // 编辑
                        BasePushBody basePushBody = BasePushBody.builder()
                                .basePushProtocol(basePushProtocol)
                                .pushOperationTypeEnum(PushOperationTypeEnum.EDIT)
                                .basePushRecord(basePushRecord)
                                .build();
                        basePushBodies.add(basePushBody);
                    }
                }
            }
        }
        // 下线
        onlineBasePushRecordMap.forEach((uniqueId, record) -> {
            if (BasePushStatusEnum.PUSH_PAUSE.getCode() == record.getPushStatus()) {
                return;
            }
            BasePushBody basePushBody = BasePushBody.builder()
                    .pushOperationTypeEnum(PushOperationTypeEnum.DOWN_LINE)
                    .basePushRecord(record)
                    .build();
            basePushBody.setBasePushRecord(record);
            basePushBodies.add(basePushBody);
        });
        return basePushBodies;
    }

    private void comparePushCount(long prePushCount, long postPushCount) {
        long preCount = prePushCount;
        if (preCount == postPushCount) {
            return;
        }
        preCount = preHandlePushCount();
        if (preCount != postPushCount) {
            log.error("businessId:{}, typeId:{}推送数量不一致，prePushCount:{}, postPushCount:{}",
                    getBasePushBusinessEnum().getCode(),
                    getSubBusinessId(),
                    prePushCount,
                    postPushCount);
        }
    }

    private void downLine(BasePushRecord record, boolean isLogToDB) {
        PushHandleContext context = pushDownLine(record);
        BasePushRecord basePushRecord = new BasePushRecord();
        basePushRecord.setRequestBody(context.getRequestBody());
        basePushRecord.setPushStatus(context.getStatus());
        basePushRecord.setResponseBody(context.getResponseBody());
        int pushStatus = basePushRecord.getPushStatus();
        if (pushStatus == BasePushStatusEnum.PUSH_END.getCode()
                || pushStatus == BasePushStatusEnum.PUSH_PAUSE.getCode()) {
            basePushRecordMapper.update(basePushRecord);
        }
        BasePushRequestLog basePushRequestLog = getBasePushRequestLog(record, context.getToken());
        savePushRequestLog(basePushRequestLog, isLogToDB);
    }

    private void savePushRequestLog(BasePushRequestLog basePushRequestLog, boolean isLogToDB) {
        log.info("BasePush--businessId:{}, typeId:{}, log:[{}]",
                getBasePushBusinessEnum().getCode(),
                getSubBusinessId(),
                JsonHelper.serialize(basePushRequestLog));
        if (isLogToDB) {
            basePushRequestLogMapper.add(basePushRequestLog);
        }
    }

    private void edit(BasePushProtocol basePushProtocol,
                      BasePushRecord basePushRecord,
                      boolean isLogToDB) {
        PushHandleContext pushRequestContext =
                pushOnline(basePushProtocol, basePushRecord.getReturnUniqueId());
        if (pushRequestContext == null) {
            return;
        }
        BasePushRecord record = new BasePushRecord();
        record.setPushStatus(pushRequestContext.getStatus());
        record.setResponseBody(pushRequestContext.getResponseBody());
        record.setRequestBody(pushRequestContext.getRequestBody());
        int status = basePushRecordMapper.update(record);
        if (status > 0) {
            BasePushRequestLog basePushRequestLog =
                    getBasePushRequestLog(record, pushRequestContext.getToken());
            savePushRequestLog(basePushRequestLog, isLogToDB);
        }
    }

    private void online(BasePushProtocol basePushProtocol, boolean isLogToDB) {
        PushHandleContext context = pushOnline(basePushProtocol, null);
        if (context == null) {
            return;
        }
        BasePushRecord basePushRecord = createBasePushRecord(basePushProtocol);
        basePushRecord.setRequestBody(context.getRequestBody());
        basePushRecord.setResponseBody(context.getResponseBody());
        boolean hasSend = context.getStatus() == 1;
        BasePushStatusEnum basePushStatusEnum = getBasePushStatusEnum(hasSend, OperationTypeEnum.ADD_OR_MODIFY);
        basePushRecord.setPushStatus(basePushStatusEnum.getCode());
        basePushRecord.setReturnUniqueId(context.getReturnUniqueId());
        int status = basePushRecordMapper.add(basePushRecord);
        if (status > 0) {
            BasePushRequestLog basePushRequestLog =
                    getBasePushRequestLog(basePushRecord, context.getToken());
            savePushRequestLog(basePushRequestLog, isLogToDB);
        }
    }

    private BasePushStatusEnum getBasePushStatusEnum(boolean hasSend,
                                                     OperationTypeEnum operationTypeEnum) {
        if (operationTypeEnum == OperationTypeEnum.DELETE) {
            return hasSend ? getEndBasePushStatusEnum() : BasePushStatusEnum.PUSHED;
        }

        boolean hasCallBack = hasCallBack();
        if (hasCallBack) {
            return hasSend ? BasePushStatusEnum.PUSH_ING : BasePushStatusEnum.NO_PUSH;
        }
        return hasSend ? BasePushStatusEnum.PUSHED : BasePushStatusEnum.NO_PUSH;
    }

    private BasePushRequestLog getBasePushRequestLog(BasePushRecord record, String token) {
        BasePushRequestLog log = new BasePushRequestLog();
        log.setBasePushRecordId(record.getId());
        log.setUuid(StringUtils.defaultString(token, ""));
        log.setCallBackResult("");
        log.setRequestBody(record.getRequestBody());
        log.setResponseBody(record.getResponseBody());
        BasePushLogStatusEnum pushLogStatusEnum = getBasePushLogStatusEnum(record.getPushStatus());
        log.setPushStatus(pushLogStatusEnum.getCode());
        return log;
    }

    private BasePushLogStatusEnum getBasePushLogStatusEnum(int pushStatus) {
        if (pushStatus == BasePushStatusEnum.NO_PUSH.getCode()) {
            return BasePushLogStatusEnum.FAIL;
        }
        if (pushStatus == BasePushStatusEnum.PUSH_ING.getCode()
                || pushStatus == BasePushStatusEnum.PUSH_END.getCode()
                || pushStatus == BasePushStatusEnum.PUSHED.getCode()
                || pushStatus == BasePushStatusEnum.PUSH_PAUSE.getCode()) {
            return BasePushLogStatusEnum.SUCCESS;
        }
        return BasePushLogStatusEnum.FAIL;
    }

    private BasePushRecord createBasePushRecord(BasePushProtocol pushProtocol) {
        BasePushRecord record = new BasePushRecord();
        record.setBusinessId(getBasePushBusinessEnum().getCode());
        record.setBrandId(pushProtocol.getBrandId());
        record.setActivityId(pushProtocol.getActivityId());
        record.setCityId(pushProtocol.getCityId());
        record.setSeriesId(pushProtocol.getSeriesId());
        record.setSubjectId(pushProtocol.getSubjectId());
        record.setTypeId(getSubBusinessId());
        record.setUniqueId(getUniqueId(pushProtocol));
        return record;
    }
}
