package com.smart4y.cloud.base.application.converter;

import com.smart4y.cloud.base.domain.model.BaseApp;
import com.smart4y.cloud.core.application.dto.AppDTO;
import com.smart4y.cloud.core.infrastructure.mapper.base.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Component
public class BaseAppConverter extends AbstractConverter<BaseApp, AppDTO> {

    @Override
    public AppDTO convert(BaseApp baseApp, Map<String, Object> parameters) {
        return new AppDTO()
                .setApiKey(baseApp.getApiKey())
                .setAppDesc(baseApp.getAppDesc())
                .setAppIcon(baseApp.getAppIcon())
                .setAppId(baseApp.getAppId())
                .setAppName(baseApp.getAppName())
                .setAppNameEn(baseApp.getAppNameEn())
                .setStatus(baseApp.getStatus())
                .setAppOs(baseApp.getAppOs())
                .setAppType(baseApp.getAppType())
                .setDeveloperId(baseApp.getDeveloperId())
                .setIsPersist(baseApp.getIsPersist())
                .setSecretKey(baseApp.getSecretKey())
                .setWebsite(baseApp.getWebsite());
    }
}