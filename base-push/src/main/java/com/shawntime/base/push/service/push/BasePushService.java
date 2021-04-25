package com.shawntime.base.push.service.push;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import com.shawntime.base.push.entity.apollo.BasePushSwitchConfig;
import com.shawntime.base.push.enums.YesOneNoZeroEnum;
import com.shawntime.base.push.service.IApolloCommonService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class BasePushService {

    @Resource
    private List<AbstractBasePushService> basePushServiceList;

    @Resource
    private IApolloCommonService apolloCommonService;

    public void push(int businessId,
                     int subBusinessId,
                     YesOneNoZeroEnum isAllEnum,
                     YesOneNoZeroEnum isDownLineEnum,
                     YesOneNoZeroEnum isCurrencyEnum,
                     YesOneNoZeroEnum isLogToDBEnum) {
        List<BasePushSwitchConfig> configs = apolloCommonService.getBasePushSwitchConfigs();
        Optional<AbstractBasePushService> optional = basePushServiceList.stream()
                .filter(basePushService -> {
                    int currBusinessId = basePushService.getBasePushBusinessEnum().getCode();
                    int currTypeId = basePushService.getSubBusinessId();
                    return businessId == currBusinessId && subBusinessId == currTypeId;
                })
                .findFirst();
        if (optional.isPresent()) {
            AbstractBasePushService basePushService = optional.get();
            BasePushSwitchConfig config = getBasePushSwitchConfig(configs, businessId, subBusinessId);
            PushMode pushMode = getPushMode(isAllEnum, isDownLineEnum, isCurrencyEnum, isLogToDBEnum, config);
            basePushService.push(pushMode);
        }
    }

    private PushMode getPushMode(YesOneNoZeroEnum isAllEnum,
                                 YesOneNoZeroEnum isDownLineEnum,
                                 YesOneNoZeroEnum isCurrencyEnum,
                                 YesOneNoZeroEnum isLogToDBEnum,
                                 BasePushSwitchConfig config) {
        PushMode pushMode = new PushMode();
        boolean isAll = isAll(isAllEnum, config);
        if (isAll) {
            pushMode.addMode(PushMode.ALLOW_ALL_UPDATE);
        }
        boolean isDownLine = isDownLine(isDownLineEnum, config);
        if (isDownLine) {
            pushMode.addMode(PushMode.ALLOW_ALL_DOWN_LINE);
        }
        boolean isLogToDB = isLogToDB(isLogToDBEnum, config);
        if (isLogToDB) {
            pushMode.addMode(PushMode.ALLOW_LOG_TO_DB);
        }
        boolean isCurrency = isCurrency(isCurrencyEnum, config);
        if (isCurrency) {
            pushMode.addMode(PushMode.ALLOW_CURRENCY);
        }
        return pushMode;
    }

    private boolean isCurrency(YesOneNoZeroEnum isCurrencyEnum, BasePushSwitchConfig config) {
        int isCurrency;
        if (isCurrencyEnum == null) {
            isCurrency = config.getIsCurrency();
        } else {
            isCurrency = isCurrencyEnum.getCode();
        }
        return isCurrency == 1;
    }

    private boolean isLogToDB(YesOneNoZeroEnum isLogToDBEnum, BasePushSwitchConfig config) {
        int isLogToDB;
        if (isLogToDBEnum == null) {
            isLogToDB = config.getIsLogToDB();
        } else {
            isLogToDB = isLogToDBEnum.getCode();
        }
        return isLogToDB == 1;
    }

    private boolean isDownLine(YesOneNoZeroEnum isDownLineEnum, BasePushSwitchConfig config) {
        int isDownLine;
        if (isDownLineEnum == null) {
            isDownLine = config.getDownLine();
        } else {
            isDownLine = isDownLineEnum.getCode();
        }
        return isDownLine == 1;
    }

    private boolean isAll(YesOneNoZeroEnum isAllEnum, BasePushSwitchConfig config) {
        int isAll;
        if (isAllEnum == null) {
            isAll = config.getIsAll();
        } else {
            isAll = isAllEnum.getCode();
        }
        return isAll == 1;
    }

    private BasePushSwitchConfig getBasePushSwitchConfig(List<BasePushSwitchConfig> configs,
                                                         int businessId,
                                                         int typeId) {
        if (CollectionUtils.isEmpty(configs)) {
            return getDefaultBasePushSwitchConfig(businessId, typeId);
        }
        Optional<BasePushSwitchConfig> optional = configs.stream()
                .filter(config -> config.getBusinessId() == businessId && config.getTypeId() == typeId)
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return getDefaultBasePushSwitchConfig(businessId, typeId);
    }

    private BasePushSwitchConfig getDefaultBasePushSwitchConfig(int businessId, int typeId) {
        BasePushSwitchConfig config = new BasePushSwitchConfig();
        config.setBusinessId(businessId);
        config.setTypeId(typeId);
        config.setDownLine(0);
        config.setIsAll(0);
        return config;
    }
}
