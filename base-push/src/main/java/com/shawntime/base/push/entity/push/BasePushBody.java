package com.shawntime.base.push.entity.push;


import com.shawntime.base.push.entity.BasePushRecord;
import com.shawntime.base.push.enums.PushOperationTypeEnum;
import lombok.Builder;

@Builder
public class BasePushBody {

    private PushOperationTypeEnum pushOperationTypeEnum;

    private BasePushProtocol basePushProtocol;

    private BasePushRecord basePushRecord;

    public PushOperationTypeEnum getPushOperationTypeEnum() {
        return pushOperationTypeEnum;
    }

    public void setPushOperationTypeEnum(PushOperationTypeEnum pushOperationTypeEnum) {
        this.pushOperationTypeEnum = pushOperationTypeEnum;
    }

    public BasePushProtocol getBasePushProtocol() {
        return basePushProtocol;
    }

    public void setBasePushProtocol(BasePushProtocol basePushProtocol) {
        this.basePushProtocol = basePushProtocol;
    }

    public BasePushRecord getBasePushRecord() {
        return basePushRecord;
    }

    public void setBasePushRecord(BasePushRecord basePushRecord) {
        this.basePushRecord = basePushRecord;
    }
}
