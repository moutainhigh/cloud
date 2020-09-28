package com.smart4y.cloud.sms.supplier.jdcloud;

import com.smart4y.cloud.sms.properties.AbstractHandlerProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 京东云短信配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.jdcloud")
public class JdCloudProperties extends AbstractHandlerProperties<String> {

    /**
     * 是否启用配置
     */
    private boolean enable = false;

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String accessKeySecret;

    /**
     * 地域
     */
    private String region = "cn-north-1";

    /**
     * 签名ID
     */
    private String signId;
}
