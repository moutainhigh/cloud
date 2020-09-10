package com.smart4y.cloud.sms.supplier.jdcloud;

import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 京东云发送端点自动配置
 */
@Configuration
@EnableConfigurationProperties(JdCloudProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class JdCloudAutoConfigure {

    /**
     * 构造京东云发送处理
     *
     * @param properties   配置对象
     * @param loadbalancer 负载均衡器
     * @return 京东云发送处理
     */
    @Bean
    @ConditionalOnProperty(value = "sms.jdcloud.enable", havingValue = "true")
    public JdCloudSendHandler jdCloudSendHandler(JdCloudProperties properties, SmsSenderLoadBalancer loadbalancer) {
        JdCloudSendHandler handler = new JdCloudSendHandler(properties);
        loadbalancer.addTarget(handler, true);
        return handler;
    }
}
