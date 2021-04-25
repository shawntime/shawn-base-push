package com.shawntime.base.push.enums;

public enum BasePushStatusEnum {
    NO_PUSH(0, "未推送"),
    PUSH_ING(1, "推送中"),
    PUSHED(2, "已上线"),
    PUSH_END(3, "已下线"),
    PUSH_PAUSE(4, "已暂停");

    private int code;
    private String description;

    private BasePushStatusEnum(int code, String description) {
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
