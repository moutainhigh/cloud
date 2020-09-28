package com.smart4y.cloud.sms.supplier.netease;

import com.smart4y.cloud.sms.properties.AbstractHandlerProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 网易云信短信配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.netease")
public class NeteaseCloudProperties extends AbstractHandlerProperties<String> {

    /**
     * 是否启用配置
     */
    private boolean enable = false;

    /**
     * appkey
     */
    private String appKey;

    /**
     * appSecret
     */
    private String appSecret;
}