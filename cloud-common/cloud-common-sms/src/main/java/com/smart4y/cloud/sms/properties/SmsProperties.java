package com.smart4y.cloud.sms.properties;

import com.smart4y.cloud.loadbalancer.LBType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信配置
 */
@Data
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    /**
     * 手机号码正则规则
     */
    private String reg;

    /**
     * 负载均衡类型
     * <p>
     * Random
     * RoundRobin:
     * WeightRandom:
     * WeightRoundRobin:
     * </p>
     */
    private LBType loadBalancerType = LBType.Random;

    /**
     * 验证码配置
     */
    private VerificationCodeProperties verificationCode = new VerificationCodeProperties();
}