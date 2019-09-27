package com.smart4y.cloud.uaa.application.impl;

import com.smart4y.cloud.uaa.infrastructure.feign.BaseAppFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    private final BaseAppFeign baseAppFeign;

    @Autowired
    public ClientDetailsServiceImpl(BaseAppFeign baseAppFeign) {
        this.baseAppFeign = baseAppFeign;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails details = baseAppFeign.getAppClientInfo(clientId).getData();
        if (details != null && details.getAdditionalInformation() != null) {
            String status = details.getAdditionalInformation().getOrDefault("status", "0").toString();
            if (!"1".equals(status)) {
                throw new ClientRegistrationException("客户端已被禁用");
            }
        }
        return details;
    }
}