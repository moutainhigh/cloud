package com.smart4y.cloud.sms.supplier.netease;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 网易云信发送端点自动配置
 */
@Configuration
@EnableConfigurationProperties(NeteaseCloudProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class NeteaseCloudAutoConfigure {

    /**
     * 构造网易云信发送处理
     *
     * @param properties   配置对象
     * @param objectMapper objectMapper
     * @param loadbalancer 负载均衡器
     * @return 网易云信发送处理
     */
    @Bean
    @Conditional(NeteaseCloudSendHandlerCondition.class)
    @ConditionalOnBean(SmsSenderLoadBalancer.class)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public NeteaseCloudSendHandler neteaseCloudSendHandler(NeteaseCloudProperties properties, ObjectMapper objectMapper, SmsSenderLoadBalancer loadbalancer) {
        NeteaseCloudSendHandler handler = new NeteaseCloudSendHandler(properties, objectMapper);
        loadbalancer.addTarget(handler, true);
        loadbalancer.setWeight(handler, properties.getWeight());
        return handler;
    }

    public static class NeteaseCloudSendHandlerCondition implements Condition {

        @SuppressWarnings("NullableProblems")
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.netease.enable", Boolean.class);
            return enable == null || enable;
        }
    }
}
