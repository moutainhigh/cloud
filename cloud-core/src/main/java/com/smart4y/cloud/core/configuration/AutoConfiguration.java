package com.smart4y.cloud.core.configuration;

import com.smart4y.cloud.core.eventhandler.RequestMappingScannedEventHandler;
import com.smart4y.cloud.core.exception.handler.OpenRestResponseErrorHandler;
import com.smart4y.cloud.core.exception.global.OpenGlobalExceptionHandler;
import com.smart4y.cloud.core.filter.XFilter;
import com.smart4y.cloud.core.properties.OpenCommonProperties;
import com.smart4y.cloud.core.properties.OpenIdGenProperties;
import com.smart4y.cloud.core.properties.OpenScanProperties;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import com.smart4y.cloud.core.security.oauth2.client.OpenOAuth2ClientProperties;
import com.smart4y.cloud.core.spring.SpringContextHolder;
import com.smart4y.cloud.core.toolkit.gen.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuate.amqp.RabbitHealthIndicator;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.boot.actuate.redis.RedisHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

/**
 * 默认配置类
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
@Slf4j
@EnableConfigurationProperties({OpenCommonProperties.class, OpenIdGenProperties.class, OpenOAuth2ClientProperties.class, OpenScanProperties.class})
public class AutoConfiguration {

    /**
     * 参数去除空格过滤
     * body缓存
     */
    @Bean
    public FilterRegistrationBean xFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>(new XFilter());
        log.info("XFilter [{}]", filterRegistrationBean);
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
     * 自定义请求资源扫描 配置
     */
    @Bean
    @ConditionalOnMissingBean(RequestMappingScannedEventHandler.class)
    public RequestMappingScannedEventHandler requestMappingScan(AmqpTemplate amqpTemplate, OpenScanProperties openScanProperties) {
        RequestMappingScannedEventHandler scan = new RequestMappingScannedEventHandler(amqpTemplate, openScanProperties);
        log.info("RequestMappingScan [{}]", scan);
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

    @Bean
    @ConditionalOnMissingBean(DataSourceHealthIndicator.class)
    public DataSourceHealthIndicator dataSourceHealthIndicator(DataSource dataSource) {
        DataSourceHealthIndicator indicator = new DataSourceHealthIndicator(dataSource);
        log.info("DataSourceHealthIndicator [{}]", indicator);
        return indicator;
    }

    @Bean
    @ConditionalOnMissingBean(RabbitHealthIndicator.class)
    public RabbitHealthIndicator rabbitHealthIndicator(RabbitTemplate rabbitTemplate) {
        RabbitHealthIndicator indicator = new RabbitHealthIndicator(rabbitTemplate);
        log.info("DataSourceHealthIndicator [{}]", indicator);
        return indicator;
    }

    @Bean
    @ConditionalOnMissingBean(RedisHealthIndicator.class)
    public RedisHealthIndicator redisHealthIndicator(RedisConnectionFactory connectionFactory) {
        RedisHealthIndicator indicator = new RedisHealthIndicator(connectionFactory);
        log.info("RedisHealthIndicator [{}]", indicator);
        return indicator;
    }
}