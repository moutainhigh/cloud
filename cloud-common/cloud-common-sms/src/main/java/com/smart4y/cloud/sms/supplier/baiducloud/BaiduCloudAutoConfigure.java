package com.smart4y.cloud.sms.supplier.baiducloud;

import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 百度云发送端点自动配置
 */
@Configuration
@EnableConfigurationProperties(BaiduCloudProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class BaiduCloudAutoConfigure {

    /**
     * 构造百度云发送处理
     *
     * @param properties   配置对象
     * @param loadbalancer 负载均衡器
     * @return 百度云发送处理
     */
    @Bean
    @ConditionalOnProperty(value = "sms.baiducloud.enable", havingValue = "true")
    public BaiduCloudSendHandler baiduCloudSendHandler(BaiduCloudProperties properties, SmsSenderLoadBalancer loadbalancer) {
        BaiduCloudSendHandler handler = new BaiduCloudSendHandler(properties);
        loadbalancer.addTarget(handler, true);
        return handler;
    }
}