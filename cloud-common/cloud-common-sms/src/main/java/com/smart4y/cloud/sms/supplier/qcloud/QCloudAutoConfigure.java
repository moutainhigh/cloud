package com.smart4y.cloud.sms.supplier.qcloud;

import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云发送端点自动配置
 */
@Configuration
@EnableConfigurationProperties(QCloudProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class QCloudAutoConfigure {

    /**
     * 构造腾讯云发送处理
     *
     * @param properties   配置对象
     * @param loadbalancer 负载均衡器
     * @return 腾讯云发送处理
     */
    @Bean
    @ConditionalOnProperty(value = "sms.qcloud.enable", havingValue = "true")
    public QCloudSendHandler qcloudSendHandler(QCloudProperties properties, SmsSenderLoadBalancer loadbalancer) {
        QCloudSendHandler handler = new QCloudSendHandler(properties);
        loadbalancer.addTarget(handler, true);
        return handler;
    }
}
