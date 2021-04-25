package com.shawntime.base.push.service.push;

import com.shawntime.base.push.enums.BasePushStatusEnum;

/**
 * @author mashaohua
 * @title: 暂停功能推送服务
 * @description: 暂停功能推送服务
 * @date 2021/4/15 16:57
 */
public abstract class AbstractPauseBasePushService extends AbstractBasePushService {

    @Override
    protected final boolean isNeedDateTimestamp() {
        return true;
    }

    @Override
    protected final BasePushStatusEnum getEndBasePushStatusEnum() {
        return BasePushStatusEnum.PUSH_PAUSE;
    }
}
