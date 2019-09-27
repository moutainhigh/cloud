package com.smart4y.cloud.uaa.interfaces.web;

import com.smart4y.cloud.core.infrastructure.security.OpenHelper;
import com.smart4y.cloud.core.infrastructure.security.oauth2.client.OpenOAuth2ClientDetails;
import com.smart4y.cloud.core.infrastructure.security.oauth2.client.OpenOAuth2ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public abstract class BaseController {

    @Autowired
    private OpenOAuth2ClientProperties clientProperties;
    @Autowired
    private AuthorizationServerEndpointsConfiguration endpoints;

    /**
     * 生成 oauth2 token
     */
    OAuth2AccessToken getToken(String userName, String password, String type) throws Exception {
        OpenOAuth2ClientDetails clientDetails = clientProperties.getOauth2().get("admin");
        // 使用oauth2密码模式登录.
        Map<String, String> postParameters = new HashMap<>();
        postParameters.put("username", userName);
        postParameters.put("password", password);
        postParameters.put("client_id", clientDetails.getClientId());
        postParameters.put("client_secret", clientDetails.getClientSecret());
        postParameters.put("grant_type", "password");
        // 添加参数区分,第三方登录
        postParameters.put("login_type", type);
        return OpenHelper.createAccessToken(endpoints, postParameters);
    }
}