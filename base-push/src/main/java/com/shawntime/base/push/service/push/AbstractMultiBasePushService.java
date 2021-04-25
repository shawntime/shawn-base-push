package com.shawntime.base.push.service.push;

import java.util.List;

import com.shawntime.base.push.entity.BasePushRecord;
import com.shawntime.base.push.entity.push.BasePushProtocol;
import com.shawntime.base.push.entity.push.PushHandleContext;

public abstract class AbstractMultiBasePushService extends AbstractBasePushService {

    @Override
    public PushHandleContext pushDownLine(BasePushRecord basePushRecord) {
        return pushHandlerService().pushDownLine(basePushRecord);
    }

    @Override
    public PushHandleContext pushOnline(BasePushProtocol basePushProtocol, String returnUniqueId) {
        return pushHandlerService().pushOnline(basePushProtocol, returnUniqueId);
    }

    @Override
    public List<BasePushProtocol> getBasePushProtocolList() {
        return pushHandlerService().getBasePushProtocolList();
    }

    protected abstract IPushHandleService pushHandlerService();

}
