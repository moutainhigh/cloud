package com.smart4y.cloud.core.security.oauth2.client;

import lombok.Data;

import java.io.Serializable;

/**
 * 社交第三方账号客户端
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
public class OpenOAuth2ClientDetails implements Serializable {

    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * 客户端授权范围
     */
    private String scope;
    /**
     * 获取token
     */
    private String accessTokenUri;
    /**
     * 认证地址
     */
    private String userAuthorizationUri;
    /**
     * 重定向地址
     */
    private String redirectUri;
    /**
     * 获取用户信息
     */
    private String userInfoUri;
    /**
     * 登录成功地址
     */
    private String loginSuccessUri;
}