package com.smart4y.cloud.spider.infrastructure.configuration;

import com.smart4y.cloud.spider.application.MessageQueueAccessLogService;
import com.smart4y.cloud.spider.infrastructure.exception.JsonAccessDeniedHandler;
import com.smart4y.cloud.spider.infrastructure.exception.JsonAuthenticationDeniedHandler;
import com.smart4y.cloud.spider.infrastructure.exception.JsonSignatureDeniedHandler;
import com.smart4y.cloud.spider.infrastructure.feign.BaseAppFeign;
import com.smart4y.cloud.spider.infrastructure.filter.AccessLogFilter;
import com.smart4y.cloud.spider.infrastructure.filter.PreCheckFilter;
import com.smart4y.cloud.spider.infrastructure.filter.PreRequestFilter;
import com.smart4y.cloud.spider.infrastructure.filter.PreSignatureFilter;
import com.smart4y.cloud.spider.infrastructure.locator.ResourceLocator;
import com.smart4y.cloud.spider.infrastructure.properties.ApiProperties;
import com.smart4y.cloud.spider.infrastructure.security.AccessManager;
import com.smart4y.cloud.spider.infrastructure.security.RedisAuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.context.SecurityContextServerWebExchange;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Oauth2资源服务器 配置
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@Configuration
public class ResourceServerConfiguration {

    private final BaseAppFeign baseAppFeign;
    private final ApiProperties apiProperties;
    private final ResourceLocator resourceLocator;
    private final MessageQueueAccessLogService messageQueueAccessLogService;
    private final RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public ResourceServerConfiguration(MessageQueueAccessLogService messageQueueAccessLogService, ApiProperties apiProperties, ResourceLocator resourceLocator, RedisConnectionFactory redisConnectionFactory, BaseAppFeign baseAppFeign) {
        this.messageQueueAccessLogService = messageQueueAccessLogService;
        this.apiProperties = apiProperties;
        this.resourceLocator = resourceLocator;
        this.redisConnectionFactory = redisConnectionFactory;
        this.baseAppFeign = baseAppFeign;
    }

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        //corsConfiguration.addExposedHeader("*");
        corsConfiguration.setMaxAge(18000L);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource
                .registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(corsConfigurationSource);
    }

    /**
     * Rest请求过滤链 配置
     */
    @Bean
    @SuppressWarnings("all")
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        // 自定义Oauth2认证, 使用Redis读取token，而非Jwt方式。
        JsonAuthenticationDeniedHandler entryPoint = new JsonAuthenticationDeniedHandler(messageQueueAccessLogService);
        JsonAccessDeniedHandler accessDeniedHandler = new JsonAccessDeniedHandler(messageQueueAccessLogService);
        AccessManager accessManager = new AccessManager(resourceLocator, apiProperties);

        AuthenticationWebFilter oauth2 = new AuthenticationWebFilter(new RedisAuthenticationManager(new RedisTokenStore(redisConnectionFactory)));
        oauth2.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
        oauth2.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(entryPoint));
        oauth2.setAuthenticationSuccessHandler((webFilterExchange, authentication) -> {
            ServerWebExchange exchange = webFilterExchange.getExchange();
            Mono<SecurityContext> subscriberContext = ReactiveSecurityContextHolder.getContext()
                    .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
            SecurityContextServerWebExchange securityContextServerWebExchange = new SecurityContextServerWebExchange(exchange, subscriberContext);
            return webFilterExchange.getChain().filter(securityContextServerWebExchange);
        });

        http
                .headers().frameOptions().disable().and()// swagger 嵌入当前页面用到此配置（iframe）
                .httpBasic().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/")
                .permitAll()
                // 动态权限验证
                .anyExchange().access(accessManager)
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(entryPoint).and()
                // 日志前置过滤器
                .addFilterAt(new PreRequestFilter(), SecurityWebFiltersOrder.FIRST)
                // 跨域过滤器
                .addFilterAt(corsWebFilter(), SecurityWebFiltersOrder.CORS)
                // 签名验证过滤器
                .addFilterAt(new PreSignatureFilter(baseAppFeign, apiProperties, new JsonSignatureDeniedHandler(messageQueueAccessLogService)), SecurityWebFiltersOrder.CSRF)
                // 访问验证前置过滤器
                .addFilterAt(new PreCheckFilter(accessManager, accessDeniedHandler), SecurityWebFiltersOrder.CSRF)
                // Oauth2认证过滤器
                .addFilterAt(oauth2, SecurityWebFiltersOrder.AUTHENTICATION)
                // 日志过滤器
                .addFilterAt(new AccessLogFilter(messageQueueAccessLogService), SecurityWebFiltersOrder.SECURITY_CONTEXT_SERVER_WEB_EXCHANGE);
        return http.build();
    }
}