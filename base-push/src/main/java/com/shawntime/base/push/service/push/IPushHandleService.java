package com.shawntime.base.push.service.push;

import java.util.List;

import com.shawntime.base.push.entity.BasePushRecord;
import com.shawntime.base.push.entity.push.BasePushProtocol;
import com.shawntime.base.push.entity.push.PushHandleContext;

/**
 * @author mashaohua
 * @title: 推送方式服务
 * @description: 推送方式服务
 * @date 2021/4/15 14:43
 */
public interface IPushHandleService {

    PushHandleContext pushDownLine(BasePushRecord basePushRecord);

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
    PushHandleContext pushOnline(BasePushProtocol basePushProtocol, String returnUniqueId);

    List<BasePushProtocol> getBasePushProtocolList();
}
