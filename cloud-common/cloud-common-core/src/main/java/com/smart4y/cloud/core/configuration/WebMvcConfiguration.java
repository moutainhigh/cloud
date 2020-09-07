package com.smart4y.cloud.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMVC 配置
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 资源处理器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry
                .addResourceHandler("swagger-ui.html", "doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 多个WebSecurityConfigurerAdapter
     */
    @Order(101)
    @Configuration
    public static class ApiWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(WebSecurity web) {
            web
                    .ignoring()
                    .antMatchers(
                            "/error",
                            "/static/**",
                            "/v2/api-docs/**",
                            "/swagger-resources/**",
                            "/webjars/**",
                            "/favicon.ico");
        }
    }
}