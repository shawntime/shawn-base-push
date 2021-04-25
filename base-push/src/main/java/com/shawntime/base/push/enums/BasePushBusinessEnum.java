package com.shawntime.base.push.enums;

public enum BasePushBusinessEnum {

    ACTIVITY_PUSH(1, "活动推送"),


    ;

    private int code;
    private String description;

    private BasePushBusinessEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
