package com.smart4y.cloud.sms.supplier.yunpian;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 云片网短信配置
 */
@Data
@ConfigurationProperties(prefix = "sms.yunpian")
public class YunPianProperties {

    /**
     * 是否启用配置
     */
    private boolean enable = true;

    /**
     * apikey
     */
    private String apikey;

    /**
     * 短信模板
     */
    private Map<String, String> templates;

    /**
     * 获取短信模板
     *
     * @param type 类型
     * @return 短信模板
     */
    public String getTemplates(String type) {
        return templates == null ? null : templates.get(type);
    }
}