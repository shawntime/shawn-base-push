package com.shawntime.base.push.enums;

public enum YesOneNoZeroEnum {
    YES(1, "是"),
    NO(0, "否");

    private int code;
    private String description;

    private YesOneNoZeroEnum(int code, String description) {
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
