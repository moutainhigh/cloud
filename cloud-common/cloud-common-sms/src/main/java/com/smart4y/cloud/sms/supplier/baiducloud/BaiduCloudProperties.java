package com.smart4y.cloud.sms.supplier.baiducloud;

import com.smart4y.cloud.sms.properties.AbstractHandlerProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 百度云短信配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.baiducloud")
public class BaiduCloudProperties extends AbstractHandlerProperties<String> {

    /**
     * 是否启用配置
     */
    private boolean enable = false;

    /**
     * ACCESS_KEY_ID
     */
    private String accessKeyId;

    /**
     * ACCESS_KEY_SECRET
     */
    private String accessKeySecret;

    /**
     * endpoint
     */
    private String endpoint;

    /**
     * 短信签名ID
     */
    private String signatureId;
}