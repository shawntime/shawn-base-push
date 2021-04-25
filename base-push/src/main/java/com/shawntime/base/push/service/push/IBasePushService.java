package com.shawntime.base.push.service.push;

/**
 * @title: 基础推送服务
 * @description: 基础推送服务
 * @author mashaohua
 * @date 2021-04-07 10:47:21
 */
public interface IBasePushService {

    /**
     * @param mode 推送模式
     */
    void push(PushMode mode);
}
