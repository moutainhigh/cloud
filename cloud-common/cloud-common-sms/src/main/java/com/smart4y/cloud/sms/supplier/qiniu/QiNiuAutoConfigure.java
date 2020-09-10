package com.smart4y.cloud.sms.supplier.qiniu;

import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云发送端点自动配置
 */
@Configuration
@EnableConfigurationProperties(QiNiuProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class QiNiuAutoConfigure {

    /**
     * 构造七牛云发送处理
     *
     * @param properties   配置对象
     * @param loadbalancer 负载均衡器
     * @return 七牛云发送处理
     */
    @Bean
    @ConditionalOnProperty(value = "sms.qiniu.enable", havingValue = "true")
    public QiNiuSendHandler qiNiuSendHandler(QiNiuProperties properties, SmsSenderLoadBalancer loadbalancer) {
        QiNiuSendHandler handler = new QiNiuSendHandler(properties);
        loadbalancer.addTarget(handler, true);
        return handler;
    }
}
