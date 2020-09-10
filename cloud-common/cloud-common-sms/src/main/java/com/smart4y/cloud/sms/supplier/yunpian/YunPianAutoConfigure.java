package com.smart4y.cloud.sms.supplier.yunpian;

import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 云片网发送端点自动配置
 */
@Configuration
@EnableConfigurationProperties(YunPianProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class YunPianAutoConfigure {

    /**
     * 构造云片网发送处理
     *
     * @param properties   配置对象
     * @param loadbalancer 负载均衡器
     * @return 云片网发送处理
     */
    @Bean
    @ConditionalOnProperty(value = "sms.yunpian.enable", havingValue = "true")
    public YunPianSendHandler yunPianSendHandler(YunPianProperties properties, SmsSenderLoadBalancer loadbalancer) {
        YunPianSendHandler handler = new YunPianSendHandler(properties);
        loadbalancer.addTarget(handler, true);
        return handler;
    }
}