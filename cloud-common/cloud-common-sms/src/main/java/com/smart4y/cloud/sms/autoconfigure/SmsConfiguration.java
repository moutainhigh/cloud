package com.smart4y.cloud.sms.autoconfigure;

import com.smart4y.cloud.sms.controller.SmsController;
import com.smart4y.cloud.sms.domain.NoticeInfo;
import com.smart4y.cloud.sms.domain.VerifyInfo;
import com.smart4y.cloud.sms.loadbalancer.RandomSmsLoadBalancer;
import com.smart4y.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import com.smart4y.cloud.sms.properties.SmsProperties;
import com.smart4y.cloud.sms.properties.SmsWebProperties;
import com.smart4y.cloud.sms.repository.VerificationCodeRepository;
import com.smart4y.cloud.sms.repository.memory.VerificationCodeMemoryRepository;
import com.smart4y.cloud.sms.repository.redis.VerificationCodeRedisRepository;
import com.smart4y.cloud.sms.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

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
    public SmsSenderLoadBalancer smsSenderLoadbalancer() {
        return new RandomSmsLoadBalancer();
    }

    /**
     * 设置控制器映射
     *
     * @param mapping       RequestMappingHandlerMapping
     * @param smsProperties 短信配置
     * @param controller    短信Controller
     * @throws NoSuchMethodException if a matching method is not found
     *                               or if the name is "&lt;init&gt;"or "&lt;clinit&gt;".
     * @throws SecurityException     If a security manager, <i>s</i>, is present and
     *                               the caller's class loader is not the same as or an
     *                               ancestor of the class loader for the current class and
     *                               invocation of {@link SecurityManager#checkPackageAccess
     *                               s.checkPackageAccess()} denies access to the package
     *                               of this class.
     */
    @Autowired(required = false)
    @ConditionalOnBean({RequestMappingHandlerMapping.class, SmsProperties.class})
    public void setWebMapping(RequestMappingHandlerMapping mapping, SmsProperties smsProperties, SmsController controller) throws NoSuchMethodException, SecurityException {
        if (smsProperties == null) {
            return;
        }
        SmsWebProperties webProperties = smsProperties.getWeb();
        if (webProperties == null || !webProperties.isEnable()) {
            return;
        }

        String bathPath = getBasePath(webProperties);

        if (webProperties.isEnableSend()) {
            Method sendMethod = SmsController.class.getMethod("sendVerificationCode", String.class);
            RequestMappingInfo sendInfo = RequestMappingInfo.paths(bathPath + "/verificationCode/{phone}")
                    .methods(RequestMethod.POST).build();
            mapping.registerMapping(sendInfo, controller, sendMethod);
        }
        if (webProperties.isEnableGet()) {
            Method getMethod = SmsController.class.getMethod("getVerificationCode", String.class, String.class);
            RequestMappingInfo getInfo = RequestMappingInfo.paths(bathPath + "/verificationCode/{phone}")
                    .methods(RequestMethod.GET).produces("application/json").build();
            mapping.registerMapping(getInfo, controller, getMethod);
        }
        if (webProperties.isEnableVerify()) {
            Method verifyMethod = SmsController.class.getMethod("verifyVerificationCode", VerifyInfo.class);
            RequestMappingInfo verifyInfo = RequestMappingInfo.paths(bathPath + "/verificationCode")
                    .methods(RequestMethod.POST).build();
            mapping.registerMapping(verifyInfo, controller, verifyMethod);
        }
        if (webProperties.isEnableNotice()) {
            Method noticeMethod = SmsController.class.getMethod("sendNotice", NoticeInfo.class);
            RequestMappingInfo noticeInfo = RequestMappingInfo.paths(bathPath + "/notice").methods(RequestMethod.PUT)
                    .build();
            mapping.registerMapping(noticeInfo, controller, noticeMethod);
        }
    }

    private String getBasePath(SmsWebProperties properties) {
        if (properties == null) {
            return SmsWebProperties.DEFAULT_BASE_PATH;
        }
        String bathPath = StringUtils.trimToNull(properties.getBasePath());
        return bathPath == null ? SmsWebProperties.DEFAULT_BASE_PATH : bathPath;
    }
}