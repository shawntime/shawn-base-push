package com.shawntime.base.push.entity.push;

import java.util.Date;

public class BasePushProtocol {

    private int subjectId;

    private int cityId;

    private int activityId;

    private int seriesId;

    private int activitySeriesId;

    private int brandId;

    private Date editTime;

    private String preferential;

    private Date startTime;

    private Date endTime;

    private int dealerInfoId;

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public int getActivitySeriesId() {
        return activitySeriesId;
    }

    public void setActivitySeriesId(int activitySeriesId) {
        this.activitySeriesId = activitySeriesId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getPreferential() {
        return preferential;
    }

    public void setPreferential(String preferential) {
        this.preferential = preferential;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getDealerInfoId() {
        return dealerInfoId;
    }

    public void setDealerInfoId(int dealerInfoId) {
        this.dealerInfoId = dealerInfoId;
    }
}
