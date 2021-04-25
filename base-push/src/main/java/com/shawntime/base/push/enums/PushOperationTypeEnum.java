package com.shawntime.base.push.enums;

public enum PushOperationTypeEnum {

    ADD(1, "新增"),
    EDIT(2, "修改"),
    DOWN_LINE(3, "下线");

    private int code;
    private String description;

    private PushOperationTypeEnum(int code, String description) {
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
