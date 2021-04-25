package com.shawntime.base.push.service.push;

import com.shawntime.base.push.enums.BasePushStatusEnum;

/**
 * @author mashaohua
 * @title: 带回调和暂停推送服务
 * @description: 带回调推送服务
 * @date 2021/4/15 16:57
 */
public abstract class AbstractCallBackPauseBasePushService extends AbstractBasePushService {

    @Override
    protected final boolean isNeedDateTimestamp() {
        return true;
    }

    @Override
    protected final BasePushStatusEnum getEndBasePushStatusEnum() {
        return BasePushStatusEnum.PUSH_PAUSE;
    }

    @Override
    protected final boolean hasCallBack() {
        return true;
    }
}
