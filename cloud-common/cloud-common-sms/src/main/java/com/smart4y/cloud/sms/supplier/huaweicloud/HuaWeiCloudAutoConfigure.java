package com.smart4y.cloud.sms.supplier.huaweicloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 华为云发送端点自动配置
 */
@Configuration
@EnableConfigurationProperties(HuaWeiCloudProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class HuaWeiCloudAutoConfigure {

    /**
     * 构造华为云发送处理
     *
     * @param properties   配置对象
     * @param objectMapper objectMapper
     * @param loadbalancer 负载均衡器
     * @return 华为云发送处理
     */
    @Bean
    @ConditionalOnProperty(value = "sms.huawei.enable", havingValue = "true")
    public HuaWeiCloudSendHandler huaWeiCloudSendHandler(HuaWeiCloudProperties properties, ObjectMapper objectMapper, SmsSenderLoadBalancer loadbalancer) {
        HuaWeiCloudSendHandler handler = new HuaWeiCloudSendHandler(properties, objectMapper);
        loadbalancer.addTarget(handler, true);
        return handler;
    }
}
