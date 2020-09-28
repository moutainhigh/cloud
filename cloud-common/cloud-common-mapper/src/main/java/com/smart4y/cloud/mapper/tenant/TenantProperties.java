package com.smart4y.cloud.mapper.tenant;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 多租户配置
 *
 * @author Youtao on 2020/9/14 16:57
 */
@Data
@Component
@ConfigurationProperties(prefix = "cloud.tenant")
public class TenantProperties {

    /**
     * 启用多租户
     */
    private boolean enable = false;

    /**
     * 租户方式（基于字段，单独的schema，单独的数据库）
     */
    private TenantType type;

    /**
     * 租户方式（基于字段，单独的schema，单独的数据库）
     */
    @Getter
    public enum TenantType {
        column, schema, datasource;
    }
}