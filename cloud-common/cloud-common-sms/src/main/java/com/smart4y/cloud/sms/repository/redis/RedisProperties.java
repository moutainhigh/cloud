package com.smart4y.cloud.sms.repository.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms.redis")
public class RedisProperties {

    /**
     * Key前缀
     */
    private String keyPrefix;
}