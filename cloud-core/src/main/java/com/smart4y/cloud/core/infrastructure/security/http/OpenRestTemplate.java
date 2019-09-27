package com.smart4y.cloud.core.infrastructure.security.http;

import com.smart4y.cloud.core.domain.event.RemoteRefreshRouteEvent;
import com.smart4y.cloud.core.infrastructure.properties.OpenCommonProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.web.client.RestTemplate;

/**
 * 自定义RestTemplate请求工具类
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class OpenRestTemplate extends RestTemplate {

    private OpenCommonProperties openCommonProperties;
    private ApplicationEventPublisher publisher;
    private BusProperties busProperties;

    public OpenRestTemplate(OpenCommonProperties openCommonProperties, BusProperties busProperties, ApplicationEventPublisher publisher) {
        this.openCommonProperties = openCommonProperties;
        this.publisher = publisher;
        this.busProperties = busProperties;
    }

    /**
     * 刷新网关
     * 注:不要频繁调用!
     * 1.资源权限发生变化时可以调用
     * 2.流量限制变化时可以调用
     * 3.IP访问发生变化时可以调用
     * 4.智能路由发生变化时可以调用
     */
    public void refreshGateway() {
        try {
            publisher.publishEvent(new RemoteRefreshRouteEvent(this, busProperties.getId(), null));
            log.info("refreshGateway:success");
        } catch (Exception e) {
            log.error("refreshGateway error:{}", e.getMessage());
        }
    }

    /**
     * 构建网关Oauth2 client_credentials方式请求
     */
    public OAuth2RestTemplate buildOAuth2ClientRequest() {
        return buildOAuth2ClientRequest(openCommonProperties.getClientId(), openCommonProperties.getClientSecret(), openCommonProperties.getAccessTokenUri());
    }

    /**
     * 构建网关Oauth2 client_credentials方式请求
     */
    public OAuth2RestTemplate buildOAuth2ClientRequest(String clientId, String clientSecret, String accessTokenUri) {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setAccessTokenUri(accessTokenUri);
        return new OAuth2RestTemplate(resource);
    }

    /**
     * 构建网关Oauth2 password方式请求
     */
    public OAuth2RestTemplate buildOAuth2PasswordRequest(String username, String password) {
        return buildOAuth2PasswordRequest(openCommonProperties.getClientId(), openCommonProperties.getClientSecret(), openCommonProperties.getAccessTokenUri(), username, password);
    }

    /**
     * 构建网关Oauth2 password方式请求
     */
    public OAuth2RestTemplate buildOAuth2PasswordRequest(String clientId, String clientSecret, String accessTokenUri, String username, String password) {
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setUsername(username);
        resource.setPassword(password);
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setAccessTokenUri(accessTokenUri);
        resource.setAuthenticationScheme(AuthenticationScheme.form);
        resource.setGrantType("password");
        return new OAuth2RestTemplate(resource);
    }
}