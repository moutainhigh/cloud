package com.smart4y.cloud.sms.supplier.upyun;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 又拍云发送端点自动配置
 */
@Configuration
@EnableConfigurationProperties(UpyunProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class UpyunAutoConfigure {

    /**
     * 构造又拍云发送处理
     *
     * @param properties   配置对象
     * @param objectMapper objectMapper
     * @param loadbalancer 负载均衡器
     * @return 又拍云发送处理
     */
    @Bean
    @ConditionalOnProperty(value = "sms.upyun.enable", havingValue = "true")
    public UpyunSendHandler upyunSendHandler(UpyunProperties properties, ObjectMapper objectMapper, SmsSenderLoadBalancer loadbalancer) {
        UpyunSendHandler handler = new UpyunSendHandler(properties, objectMapper);
        loadbalancer.addTarget(handler, true);
        return handler;
    }
}
