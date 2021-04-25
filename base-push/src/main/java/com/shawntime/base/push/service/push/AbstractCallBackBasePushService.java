package com.shawntime.base.push.service.push;

/**
 * @author mashaohua
 * @title: 带回调推送服务
 * @description: 带回调推送服务
 * @date 2021/4/15 16:57
 */
public abstract class AbstractCallBackBasePushService extends AbstractBasePushService {

    @Override
    protected final boolean hasCallBack() {
        return true;
    }
}
