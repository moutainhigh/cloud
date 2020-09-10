package com.smart4y.cloud.sms.supplier.aliyun;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云发送端点自动配置
 */
@Configuration
@EnableConfigurationProperties(AliyunProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class AliyunAutoConfigure {

    /**
     * 构造阿里云发送处理
     *
     * @param properties   配置对象
     * @param objectMapper objectMapper
     * @param loadbalancer 负载均衡器
     * @return 阿里云发送处理
     */
    @Bean
    @ConditionalOnProperty(value = "sms.aliyun.enable", havingValue = "true")
    public AliyunSendHandler aliyunSendHandler(AliyunProperties properties, ObjectMapper objectMapper, SmsSenderLoadBalancer loadbalancer) {
        AliyunSendHandler handler = new AliyunSendHandler(properties, objectMapper);
        loadbalancer.addTarget(handler, true);
        return handler;
    }
}