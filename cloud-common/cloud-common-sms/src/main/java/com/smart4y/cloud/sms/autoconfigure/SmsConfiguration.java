package com.smart4y.cloud.sms.autoconfigure;

import com.smart4y.cloud.sms.loadbalancer.*;
import com.smart4y.cloud.sms.properties.SmsProperties;
import com.smart4y.cloud.sms.repository.VerificationCodeRepository;
import com.smart4y.cloud.sms.repository.memory.VerificationCodeMemoryRepository;
import com.smart4y.cloud.sms.repository.redis.VerificationCodeRedisRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 短信服务配置
 */
@Configuration
@ComponentScan({
        "com.smart4y.cloud.sms.controller",
        "com.smart4y.cloud.sms.repository",
        "com.smart4y.cloud.sms.service"
})
@EnableConfigurationProperties({SmsProperties.class})
public class SmsConfiguration {

    /**
     * 构造默认验证码储存接口实现
     *
     * @return 默认验证码储存接口实现
     */
    @Bean
    @ConditionalOnMissingBean(VerificationCodeRepository.class)
    public VerificationCodeRepository verificationCodeRepository(SmsProperties properties) {
        String repository = properties.getVerificationCode().getRepository();
        if ("redis".equals(repository)) {
            return new VerificationCodeRedisRepository();
        } else {
            return new VerificationCodeMemoryRepository();
        }
    }

    /**
     * 构造发送者负载均衡器
     *
     * @return 发送者负载均衡器
     */
    @Bean
    @ConditionalOnMissingBean(SmsSenderLoadBalancer.class)
    public SmsSenderLoadBalancer smsSenderLoadBalancer(SmsProperties properties) {
        switch (properties.getLoadBalancerType()) {
            case RoundRobin:
                return new RoundRobinSmsLoadBalancer();
            case WeightRandom:
                return new WeightRandomSmsLoadBalancer();
            case WeightRoundRobin:
                return new WeightRoundRobinSmsLoadBalancer();
            case Hash:
            case Random:
            case SmoothWeightRoundRobin:
            default:
                return new RandomSmsLoadBalancer();
        }
    }
}