package com.smart4y.cloud.core.infrastructure.autoconfigure;

import com.smart4y.cloud.core.application.eventhandler.ResourceAnnotationScannedEventHandler;
import com.smart4y.cloud.core.infrastructure.exception.OpenGlobalExceptionHandler;
import com.smart4y.cloud.core.infrastructure.exception.OpenRestResponseErrorHandler;
import com.smart4y.cloud.core.infrastructure.filter.XssFilter;
import com.smart4y.cloud.core.infrastructure.health.DbHealthIndicator;
import com.smart4y.cloud.core.infrastructure.properties.OpenCommonProperties;
import com.smart4y.cloud.core.infrastructure.properties.OpenIdGenProperties;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
import com.smart4y.cloud.core.infrastructure.security.oauth2.client.OpenOAuth2ClientProperties;
import com.smart4y.cloud.core.infrastructure.spring.SpringContextHolder;
import com.smart4y.cloud.core.infrastructure.toolkit.gen.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * 默认配置类
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
@Slf4j
@EnableConfigurationProperties({OpenCommonProperties.class, OpenIdGenProperties.class, OpenOAuth2ClientProperties.class})
public class AutoConfiguration {

    /**
     * XSS过滤 配置
     * body缓存
     */
    @Bean
    public FilterRegistrationBean xssFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>(new XssFilter());
        log.info("XssFilter [{}]", filterRegistrationBean);
        return filterRegistrationBean;
    }

    /**
     * 默认加密 配置
     */
    @Bean
    @ConditionalOnMissingBean(BCryptPasswordEncoder.class)
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info("BCryptPasswordEncoder [{}]", encoder);
        return encoder;
    }

    /**
     * Spring上下文工具 配置
     */
    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        log.info("SpringContextHolder [{}]", holder);
        return holder;
    }

    /**
     * 统一异常处理 配置
     */
    @Bean
    @ConditionalOnMissingBean(OpenGlobalExceptionHandler.class)
    public OpenGlobalExceptionHandler exceptionHandler() {
        OpenGlobalExceptionHandler exceptionHandler = new OpenGlobalExceptionHandler();
        log.info("OpenGlobalExceptionHandler [{}]", exceptionHandler);
        return exceptionHandler;
    }

    /**
     * ID生成器配置
     */
    @Bean
    @ConditionalOnMissingBean(OpenIdGenProperties.class)
    public SnowflakeIdWorker snowflakeIdWorker(OpenIdGenProperties properties) {
        SnowflakeIdWorker worker = new SnowflakeIdWorker(properties.getWorkId(), properties.getCenterId());
        log.info("SnowflakeIdWorker [{}]", worker);
        return worker;
    }

    /**
     * 自定义注解扫描 配置
     */
    @Bean
    @ConditionalOnMissingBean(ResourceAnnotationScannedEventHandler.class)
    public ResourceAnnotationScannedEventHandler resourceAnnotationScan(AmqpTemplate amqpTemplate) {
        ResourceAnnotationScannedEventHandler scan = new ResourceAnnotationScannedEventHandler(amqpTemplate);
        log.info("ResourceAnnotationScan [{}]", scan);
        return scan;
    }

    /**
     * 自定义Oauth2请求类 配置
     */
    @Bean
    @ConditionalOnMissingBean(OpenRestTemplate.class)
    public OpenRestTemplate openRestTemplate(OpenCommonProperties openCommonProperties, BusProperties busProperties, ApplicationEventPublisher publisher) {
        OpenRestTemplate restTemplate = new OpenRestTemplate(openCommonProperties, busProperties, publisher);
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new OpenRestResponseErrorHandler());
        log.info("OpenRestTemplate [{}]", restTemplate);
        return restTemplate;
    }

    /**
     * 请求远程服务 配置
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // 设置自定义ErrorHandler
        restTemplate.setErrorHandler(new OpenRestResponseErrorHandler());
        log.info("RestTemplate [{}]", restTemplate);
        return restTemplate;
    }

    /**
     * 服务健康检查 配置
     */
    @Bean
    @ConditionalOnMissingBean(DbHealthIndicator.class)
    public DbHealthIndicator dbHealthIndicator() {
        DbHealthIndicator dbHealthIndicator = new DbHealthIndicator();
        log.info("DbHealthIndicator [{}]", dbHealthIndicator);
        return dbHealthIndicator;
    }
}