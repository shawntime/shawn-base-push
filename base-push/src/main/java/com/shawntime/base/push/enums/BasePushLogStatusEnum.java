package com.shawntime.base.push.enums;

public enum BasePushLogStatusEnum {

    FAIL(0, "推送失败"),
    SUCCESS(1, "推送成功"),
    CALLBACK(3, "已回调");

    private int code;
    private String description;

    private BasePushLogStatusEnum(int code, String description) {
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