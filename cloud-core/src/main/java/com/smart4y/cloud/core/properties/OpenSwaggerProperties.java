package com.smart4y.cloud.core.properties;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 自定义Swagger配置
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
@ConfigurationProperties(prefix = "cloud.swagger2")
public class OpenSwaggerProperties {

    /**
     * 是否启用swagger,生产环境建议关闭
     */
    private boolean enabled;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档描述
     */
    private String description;
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
     * 忽略列表
     */
    private List<String> ignores = Lists.newArrayList();
}