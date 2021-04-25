package com.shawntime.base.push.entity.push;

import lombok.Builder;

/**
 * @author mashaohua
 * @title: 推送处理上下文
 * @description: 推送处理上下文
 * @date 2021/4/15 14:46
 * @menu
 */
@Builder
public class PushHandleContext {

    private String requestBody;

    private String responseBody;

    /**
     * 请求状态，1：成功，0：失败
     */
    private int status;

    /**
     * 被调用方返回的唯一标识
     */
    private String returnUniqueId;

    /**
     * 回调使用，每一次调用请求唯一标识
     */
    private String token;

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReturnUniqueId() {
        return returnUniqueId;
    }

    public void setReturnUniqueId(String returnUniqueId) {
        this.returnUniqueId = returnUniqueId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
