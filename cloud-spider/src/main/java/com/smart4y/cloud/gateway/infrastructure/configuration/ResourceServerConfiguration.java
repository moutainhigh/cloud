package com.smart4y.cloud.gateway.infrastructure.configuration;

import com.smart4y.cloud.gateway.infrastructure.filter.AccessManager;
import com.smart4y.cloud.gateway.infrastructure.exception.JsonAccessDeniedHandler;
import com.smart4y.cloud.gateway.infrastructure.exception.JsonAuthenticationDeniedHandler;
import com.smart4y.cloud.gateway.infrastructure.exception.JsonSignatureDeniedHandler;
import com.smart4y.cloud.gateway.infrastructure.service.feign.BaseAppServiceClient;
import com.smart4y.cloud.gateway.infrastructure.filter.AccessLogFilter;
import com.smart4y.cloud.gateway.infrastructure.filter.PreCheckFilter;
import com.smart4y.cloud.gateway.infrastructure.filter.PreRequestFilter;
import com.smart4y.cloud.gateway.infrastructure.filter.PreSignatureFilter;
import com.smart4y.cloud.gateway.infrastructure.locator.ResourceLocator;
import com.smart4y.cloud.gateway.infrastructure.service.JdbcAccessLogService;
import com.smart4y.cloud.gateway.infrastructure.oauth2.RedisAuthenticationManager;
import com.smart4y.cloud.gateway.infrastructure.properties.ApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Oauth2资源服务器 配置
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Configuration
public class ResourceServerConfiguration {

    private static final String CORS_MAX_AGE = "18000L";

    private final BaseAppServiceClient baseAppServiceClient;
    private final ApiProperties apiProperties;
    private final ResourceLocator resourceLocator;
    private final JdbcAccessLogService accessLogService;
    private final RedisConnectionFactory redisConnectionFactory;

    @Autowired
    @SuppressWarnings("all")
    public ResourceServerConfiguration(JdbcAccessLogService accessLogService, ApiProperties apiProperties, ResourceLocator resourceLocator, RedisConnectionFactory redisConnectionFactory, BaseAppServiceClient baseAppServiceClient) {
        this.accessLogService = accessLogService;
        this.apiProperties = apiProperties;
        this.resourceLocator = resourceLocator;
        this.redisConnectionFactory = redisConnectionFactory;
        this.baseAppServiceClient = baseAppServiceClient;
    }

    /**
     * CORS跨域 配置
     */
    private WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                HttpHeaders requestHeaders = request.getHeaders();
                ServerHttpResponse response = ctx.getResponse();
                HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
                HttpHeaders headers = response.getHeaders();
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
                headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
                if (requestMethod != null) {
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
                }
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
                headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, CORS_MAX_AGE);
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

    /**
     * Rest请求过滤链 配置
     */
    @Bean
    @SuppressWarnings("all")
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        // 自定义Oauth2认证, 使用Redis读取token，而非Jwt方式。
        JsonAuthenticationDeniedHandler entryPoint = new JsonAuthenticationDeniedHandler(accessLogService);
        JsonAccessDeniedHandler accessDeniedHandler = new JsonAccessDeniedHandler(accessLogService);
        AccessManager accessManager = new AccessManager(resourceLocator, apiProperties);
        AuthenticationWebFilter oauth2 = new AuthenticationWebFilter(new RedisAuthenticationManager(new RedisTokenStore(redisConnectionFactory)));
        oauth2.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
        oauth2.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(entryPoint));
        oauth2.setAuthenticationSuccessHandler((webFilterExchange, authentication) -> {
            ServerWebExchange exchange = webFilterExchange.getExchange();
            Mono<SecurityContext> subscriberContext = ReactiveSecurityContextHolder.getContext().subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
            SecurityContextServerWebExchange securityContextServerWebExchange = new SecurityContextServerWebExchange(exchange, subscriberContext);
            return webFilterExchange.getChain().filter(securityContextServerWebExchange);
        });

        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/").permitAll()
                // 动态权限验证
                .anyExchange().access(accessManager)
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(entryPoint).and()
                // 日志前置过滤器
                .addFilterAt(new PreRequestFilter(), SecurityWebFiltersOrder.FIRST)
                // 跨域过滤器
                .addFilterAt(corsFilter(), SecurityWebFiltersOrder.CORS)
                // 签名验证过滤器
                .addFilterAt(new PreSignatureFilter(baseAppServiceClient, apiProperties, new JsonSignatureDeniedHandler(accessLogService)), SecurityWebFiltersOrder.CSRF)
                // 访问验证前置过滤器
                .addFilterAt(new PreCheckFilter(accessManager, accessDeniedHandler), SecurityWebFiltersOrder.CSRF)
                // Oauth2认证过滤器
                .addFilterAt(oauth2, SecurityWebFiltersOrder.AUTHENTICATION)
                // 日志过滤器
                .addFilterAt(new AccessLogFilter(accessLogService), SecurityWebFiltersOrder.SECURITY_CONTEXT_SERVER_WEB_EXCHANGE);
        return http.build();
    }
}