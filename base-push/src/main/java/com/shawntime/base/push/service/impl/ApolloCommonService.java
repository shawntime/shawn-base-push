package com.shawntime.base.push.service.impl;

import java.util.List;
import javax.annotation.Resource;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.dto.ApolloConfig;
import com.shawntime.base.push.entity.apollo.BasePushSwitchConfig;
import com.shawntime.base.push.service.IApolloCommonService;
import com.shawntime.base.push.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApolloCommonService implements IApolloCommonService {

    @Resource
    private ApolloConfig apolloConfig;

    @Override
    public List<BasePushSwitchConfig> getBasePushSwitchConfigs() {
        try {
            Config appConfig = ConfigService.getAppConfig();
            String json = appConfig.getProperty("base.push.switch.config", null);
            if (StringUtils.isEmpty(json)) {
                return null;
            }
            return JsonHelper.deSerializeList(json, BasePushSwitchConfig.class);
        } catch (Exception e) {
            log.error("获取apollo参数基础推送配置信息异常", e);
            return null;
        }
    }

}
