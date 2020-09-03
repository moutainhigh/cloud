package com.smart4y.cloud.base.infrastructure.configuration;

import com.smart4y.cloud.core.configuration.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-11.
 */
@Configuration
@Import(value = {
        AutoConfiguration.class,
        FeignAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        MqAutoConfiguration.class,
        RedisCacheAutoConfiguration.class,
        SwaggerAutoConfiguration.class,
        WebMvcConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
public class AutoConfig {
}