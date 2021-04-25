package com.shawntime.base.push.entity.apollo;

public class BasePushSwitchConfig {

    private int businessId;

    private int typeId;

    private int isAll;

    private int downLine;

    private int isLogToDB = 1;

    private int isCurrency;

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getIsAll() {
        return isAll;
    }

    public void setIsAll(int isAll) {
        this.isAll = isAll;
    }

    public int getDownLine() {
        return downLine;
    }

    public void setDownLine(int downLine) {
        this.downLine = downLine;
    }

    public int getIsLogToDB() {
        return isLogToDB;
    }

    public void setIsLogToDB(int isLogToDB) {
        this.isLogToDB = isLogToDB;
    }

    public int getIsCurrency() {
        return isCurrency;
    }

    public void setIsCurrency(int isCurrency) {
        this.isCurrency = isCurrency;
    }
}
