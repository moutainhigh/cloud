package com.smart4y.cloud.sms.supplier.upyun;

import com.smart4y.cloud.sms.properties.AbstractHandlerProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 又拍云短信配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.upyun")
public class UpyunProperties extends AbstractHandlerProperties<String> {

    /**
     * 是否启用配置
     */
    private boolean enable = false;

    /**
     * token
     */
    private String token;
}
