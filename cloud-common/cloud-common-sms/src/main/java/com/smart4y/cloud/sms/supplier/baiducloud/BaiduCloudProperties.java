package com.smart4y.cloud.sms.supplier.baiducloud;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 百度云短信配置
 */
@Data
@ConfigurationProperties(prefix = "sms.baiducloud")
public class BaiduCloudProperties {

    /**
     * 是否启用配置
     */
    private boolean enable = true;

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