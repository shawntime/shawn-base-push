package com.shawntime.base.push.enums;

public enum OperationTypeEnum {

    ADD_OR_MODIFY(1, "新增或修改"),

    DELETE(2, "删除");

    private int code;

    private String description;

    OperationTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
