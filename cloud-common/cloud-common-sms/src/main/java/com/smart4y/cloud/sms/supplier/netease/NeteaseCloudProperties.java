package com.smart4y.cloud.sms.supplier.netease;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * 网易云信短信配置
 */
@Data
@ConfigurationProperties(prefix = "sms.netease")
public class NeteaseCloudProperties {

    /**
     * 是否启用配置
     */
    private boolean enable = true;

    /**
     * appkey
     */
    private String appKey;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * 短信模板
     */
    protected Map<String, String> templates;

    /**
     * 参数顺序
     */
    protected Map<String, List<String>> paramsOrders;

    /**
     * 获取短信模板
     *
     * @param type 类型
     * @return 短信模板
     */
    public String getTemplates(String type) {
        return templates == null ? null : templates.get(type);
    }

    /**
     * 返回参数顺序
     *
     * @param type 类型
     * @return 参数顺序
     */
    public List<String> getParamsOrder(String type) {
        return paramsOrders.get(type);
    }
}