package com.shawntime.base.push.service.push;

import com.shawntime.base.push.enums.BasePushStatusEnum;

public abstract class AbstractMultiCallBackPauseBasePushService extends AbstractMultiBasePushService {

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
