package com.smart4y.cloud.sms.supplier.baiducloud;

import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

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
    @Conditional(BaiduCloudSendHandlerCondition.class)
    @ConditionalOnBean(SmsSenderLoadBalancer.class)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BaiduCloudSendHandler baiduCloudSendHandler(BaiduCloudProperties properties, SmsSenderLoadBalancer loadbalancer) {
        BaiduCloudSendHandler handler = new BaiduCloudSendHandler(properties);
        loadbalancer.addTarget(handler, true);
        loadbalancer.setWeight(handler, properties.getWeight());
        return handler;
    }

    public static class BaiduCloudSendHandlerCondition implements Condition {

        @Override
        @SuppressWarnings("NullableProblems")
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.baiducloud.enable", Boolean.class);
            return enable == null || enable;
        }
    }
}