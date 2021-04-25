package com.shawntime.base.push.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础推送记
 *
 * @author LemonGenerator
 */
@Getter
@Setter
public class BasePushRecord {

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 业务线id，1：车商汇下单页推送 2: 智能车展广告创意3:车展活动推送PMO线索收集任务  4：PMO活动入库  6：营销活动正式活动-预上线任务推送PMO线索收集任务  7：开通小视频更新活动时间
     */
    private Integer businessId;
    /**
     * 业务线下的子类型
     */
    private Integer typeId;
    /**
     * 专题id
     */
    private Integer subjectId;
    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 品牌id
     */
    private Integer brandId;
    /**
     * 车系id
     */
    private Integer seriesId;
    /**
     * 请求唯一标识
     */
    private String uniqueId;
    /**
     * 请求内容
     */
    private String requestBody;
    /**
     * 响应内容
     */
    private String responseBody;
    /**
     * 推送专题，0：未推送，1：推送中，2：已推送，3：已下线BasePushStatusEnum
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
    /**
     * 接口返回唯一id
     */
    private String returnUniqueId;

}
