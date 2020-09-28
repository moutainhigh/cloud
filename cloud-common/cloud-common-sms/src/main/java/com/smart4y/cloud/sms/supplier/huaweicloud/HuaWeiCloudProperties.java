package com.smart4y.cloud.sms.supplier.huaweicloud;

import com.smart4y.cloud.sms.properties.AbstractHandlerProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 华为云短信配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.huawei")
public class HuaWeiCloudProperties extends AbstractHandlerProperties<String> {

    /**
     * 是否启用配置
     */
    private boolean enable = false;

    /**
     * 请求地址
     */
    private String uri;

    /**
     * APP_Key
     */
    private String appKey;

    /**
     * APP_Secret
     */
    private String appSecret;

    /**
     * 国内短信签名通道号或国际/港澳台短信通道号
     */
    private String sender;

    /**
     * 签名名称
     */
    private String signature;
}