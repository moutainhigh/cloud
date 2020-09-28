package com.smart4y.cloud.sms.supplier.yunpian;

import com.smart4y.cloud.sms.properties.AbstractHandlerProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 云片网短信配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.yunpian")
public class YunPianProperties extends AbstractHandlerProperties<String> {

    /**
     * 是否启用配置
     */
    private boolean enable = false;

    /**
     * apikey
     */
    private String apikey;
}