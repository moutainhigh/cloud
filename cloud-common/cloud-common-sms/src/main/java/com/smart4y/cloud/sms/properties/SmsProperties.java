package com.smart4y.cloud.sms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信配置
 */
@Data
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    /**
     * 手机号码正则规则
     */
    private String reg;

    /**
     * 验证码配置
     */
    private VerificationCodeProperties verificationCode = new VerificationCodeProperties();
}