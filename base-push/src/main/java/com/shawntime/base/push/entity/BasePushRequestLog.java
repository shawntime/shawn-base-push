package com.shawntime.base.push.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础推送请
 *
 * @author LemonGenerator
 */
@Getter
@Setter
public class BasePushRequestLog {

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 基础推送记录表主键id
     */
    private Integer basePushRecordId;
    /**
     * 请求唯一标识
     */
    private String uuid;
    /**
     * 请求体内容
     */
    private String requestBody;
    /**
     * 请求响应内容
     */
    private String responseBody;
    /**
     * 回调内容
     */
    private String callBackResult;
    /**
     * 推送专题，0：推送失败，1：已推送，3：已回调
     */
    private Integer pushStatus;
    /**
     * 创建时间
     */
    private Date createdStime;
    /**
     * 编辑时间
     */
    private Date modifiedStime;
    /**
     * 是否删除
     */
    private Integer isDel;

}