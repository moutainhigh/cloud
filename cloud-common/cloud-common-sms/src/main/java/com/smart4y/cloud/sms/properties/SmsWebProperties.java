package com.smart4y.cloud.sms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信Web配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms.web")
public class SmsWebProperties {

    /**
     * 默认基础路径
     */
    public static final String DEFAULT_BASE_PATH = "/sms";

    /**
     * 基础路径
     */
    private String basePath = DEFAULT_BASE_PATH;

    /**
     * 是否启用web端点
     */
    private boolean enable = false;

    /**
     * 是否启用验证码发送web端点
     */
    private boolean enableSend = true;

    /**
     * 是否启用验证码查询web端点
     */
    private boolean enableGet = true;

    /**
     * 是否启用验证码验证web端点
     */
    private boolean enableVerify = true;

    /**
     * 是否启用通知发送web端点
     */
    private boolean enableNotice = true;
}