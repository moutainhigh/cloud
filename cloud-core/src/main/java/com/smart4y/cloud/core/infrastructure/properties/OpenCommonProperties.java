package com.smart4y.cloud.core.infrastructure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义网关配置
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
@ConfigurationProperties(prefix = "cloud.common")
public class OpenCommonProperties {

    /**
     * 网关客户端Id
     */
    private String clientId;
    /**
     * 网关客户端密钥
     */
    private String clientSecret;
    /**
     * 网关服务地址
     */
    private String apiServerAddr;
    /**
     * 平台认证服务地址
     */
    private String authServerAddr;
    /**
     * 后台部署地址
     */
    private String adminServerAddr;
    /**
     * 认证范围
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
     * 获取token地址
     */
    private String tokenInfoUri;
    /**
     * 获取用户信息地址
     */
    private String userInfoUri;
    /**
     * JWT签名key
     */
    private String jwtSigningKey;
}