package com.shawntime.base.push.service.push;

public abstract class AbstractMultiCallBackBasePushService extends AbstractMultiBasePushService {

    @Override
    protected final boolean hasCallBack() {
        return true;
    }

}
