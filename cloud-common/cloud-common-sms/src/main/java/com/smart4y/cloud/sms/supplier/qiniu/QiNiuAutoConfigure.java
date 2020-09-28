package com.smart4y.cloud.sms.supplier.qiniu;

import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

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
    @Conditional(QiNiuSendHandlerCondition.class)
    @ConditionalOnBean(SmsSenderLoadBalancer.class)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public QiNiuSendHandler qiNiuSendHandler(QiNiuProperties properties, SmsSenderLoadBalancer loadbalancer) {
        QiNiuSendHandler handler = new QiNiuSendHandler(properties);
        loadbalancer.addTarget(handler, true);
        loadbalancer.setWeight(handler, properties.getWeight());
        return handler;
    }

    public static class QiNiuSendHandlerCondition implements Condition {

        @SuppressWarnings("NullableProblems")
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.qiniu.enable", Boolean.class);
            return enable == null || enable;
        }
    }
}
