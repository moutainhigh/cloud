package com.smart4y.cloud.base.application.impl;

import com.smart4y.cloud.base.application.AppService;
import com.smart4y.cloud.base.application.converter.BaseAppConverter;
import com.smart4y.cloud.base.domain.model.BaseApp;
import com.smart4y.cloud.base.domain.service.BaseAppDomainService;
import com.smart4y.cloud.base.domain.service.BaseAuthorityDomainService;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.application.dto.AppDTO;
import com.smart4y.cloud.core.infrastructure.security.OpenClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Slf4j
@ApplicationService
public class AppServiceImpl implements AppService {

    private final BaseAppConverter baseAppConverter;
    private final BaseAppDomainService baseAppDomainService;
    private final JdbcClientDetailsService jdbcClientDetailsService;
    private final BaseAuthorityDomainService baseAuthorityDomainService;

    @Autowired
    public AppServiceImpl(BaseAppDomainService baseAppDomainService, BaseAppConverter baseAppConverter, JdbcClientDetailsService jdbcClientDetailsService, BaseAuthorityDomainService baseAuthorityDomainService) {
        this.baseAppDomainService = baseAppDomainService;
        this.baseAppConverter = baseAppConverter;
        this.jdbcClientDetailsService = jdbcClientDetailsService;
        this.baseAuthorityDomainService = baseAuthorityDomainService;
    }

    @Override
    public AppDTO getAppInfo(String appId) {
        BaseApp baseApp = baseAppDomainService.getById(appId);
        return baseAppConverter.convert(baseApp);
    }

    @Override
    public OpenClientDetails getAppClientInfo(String clientId) {
        BaseClientDetails baseClientDetails = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(clientId);
        if (baseClientDetails == null) {
            return null;
        }
        String appId = baseClientDetails.getAdditionalInformation().get("appId").toString();

        OpenClientDetails openClient = new OpenClientDetails();
        BeanUtils.copyProperties(baseClientDetails, openClient);
        openClient.setAuthorities(baseAuthorityDomainService.getAppAuthorities(appId));
        return openClient;
    }
}