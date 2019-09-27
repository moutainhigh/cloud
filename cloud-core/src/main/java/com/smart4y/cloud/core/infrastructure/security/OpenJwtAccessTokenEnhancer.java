package com.smart4y.cloud.core.infrastructure.security;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义JwtAccessToken转换器
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class OpenJwtAccessTokenEnhancer extends JwtAccessTokenConverter {

    /**
     * 生成token
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof OpenUserDetails) {
            // 设置额外用户信息
            OpenUserDetails baseUser = ((OpenUserDetails) authentication.getPrincipal());
            final Map<String, Object> additionalInfo = new HashMap<>(8);
            additionalInfo.put(OpenSecurityConstants.OPEN_ID, baseUser.getUserId());
            additionalInfo.put(OpenSecurityConstants.DOMAIN, baseUser.getDomain());
            defaultOAuth2AccessToken.setAdditionalInformation(additionalInfo);
        }

        return super.enhance(defaultOAuth2AccessToken, authentication);
    }

    /**
     * 解析token
     */
    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
        return super.extractAccessToken(value, map);
    }
}