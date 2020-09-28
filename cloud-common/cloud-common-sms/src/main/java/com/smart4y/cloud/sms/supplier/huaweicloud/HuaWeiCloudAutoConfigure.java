package com.smart4y.cloud.sms.supplier.huaweicloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

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
    @Conditional(HuaWeiCloudSendHandlerCondition.class)
    @ConditionalOnBean(SmsSenderLoadBalancer.class)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public HuaWeiCloudSendHandler huaWeiCloudSendHandler(HuaWeiCloudProperties properties, ObjectMapper objectMapper, SmsSenderLoadBalancer loadbalancer) {
        HuaWeiCloudSendHandler handler = new HuaWeiCloudSendHandler(properties, objectMapper);
        loadbalancer.addTarget(handler, true);
        loadbalancer.setWeight(handler, properties.getWeight());
        return handler;
    }

    public static class HuaWeiCloudSendHandlerCondition implements Condition {

        @Override
        @SuppressWarnings("NullableProblems")
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.huawei.enable", Boolean.class);
            return enable == null || enable;
        }
    }
}
