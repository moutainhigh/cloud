package com.smart4y.cloud.spider.application;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.smart4y.cloud.core.interceptor.FeignRequestInterceptor;
import com.smart4y.cloud.core.security.OpenUserDetails;
import com.smart4y.cloud.core.toolkit.Kit;
import com.smart4y.cloud.spider.domain.GatewayContext;
import com.smart4y.cloud.spider.infrastructure.toolkit.ReactiveWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * 网关日志记录 实现类
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@Component
public class MessageQueueAccessLogService implements AccessLogService {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Value("${spring.application.name}")
    private String defaultServiceId;

    /**
     * 不记录日志的请求列表
     */
    private final List<String> ignores = Arrays.asList(
            "/**/oauth/check_token/**",
            "/**/gateway/access/logs/**",
            "/webjars/**");

    @Override
    public void sendLog(ServerWebExchange exchange, Exception ex) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String requestPath = request.getURI().getPath();
        if (ignore(requestPath)) {
            return;
        }

        String serviceId = getServiceId(exchange);
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        String userAgent = headers.get(HttpHeaders.USER_AGENT);
        String ip = ReactiveWebUtils.getRemoteAddress(exchange);
        LocalDateTime requestTime = exchange.getAttribute(FeignRequestInterceptor.X_REQUEST_TIME);
        if (null == requestTime) {
            requestTime = LocalDateTime.now();
        }
        String method = request.getMethodValue();
        Map<String, String> data = getParams(exchange);
        String error = getErrorMessage(ex);
        // String authentication = getAuthentication(exchange);
        int httpStatus = Objects.requireNonNull(response.getStatusCode()).value();
        LocalDateTime responseTime = LocalDateTime.now();

        try {
            Map<String, Object> logs = new HashMap<>();
            logs.put("serviceId", serviceId);
            logs.put("ip", ip);
            logs.put("userAgent", Kit.help().json().toJson(userAgent));
            logs.put("headers", headers);
            logs.put("requestTime", requestTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            logs.put("method", method);
            logs.put("data", Kit.help().json().toJson(data));
            logs.put("error", error);
            logs.put("httpStatus", httpStatus);
            logs.put("responseTime", responseTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            logs.put("useMillis", ChronoUnit.MILLIS.between(requestTime, responseTime));
            log.info(Kit.help().json().toJson(logs));
        } catch (Exception e) {
            log.error("Access log save error {}", e.getMessage(), e);
        }
    }

    /**
     * 是否忽略记录日志
     */
    private boolean ignore(String requestPath) {
        return ignores.stream()
                .anyMatch(path -> antPathMatcher.match(path, requestPath));
    }

    /**
     * 获取错误信息
     */
    private String getErrorMessage(Exception ex) {
        String error = null;
        if (ex != null) {
            error = ex.getMessage();
        }
        return error;
    }

    /**
     * 获取服务ID
     */
    private String getServiceId(ServerWebExchange exchange) {
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        String serviceId = null;
        if (route != null) {
            serviceId = route.getUri().toString().replace("lb://", "");
        }
        if (null == serviceId) {
            serviceId = defaultServiceId;
        }
        return serviceId;
    }

    /**
     * 获取所有请求参数
     */
    private Map<String, String> getParams(ServerWebExchange exchange) {
        Map<String, String> data = Maps.newHashMap();
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null) {
            data = gatewayContext.getAllRequestData().toSingleValueMap();
        }
        return data;
    }

    /**
     * 获取认证用户信息
     */
    private String getAuthentication(ServerWebExchange exchange) {
        String key = "authentication";
        Mono<Authentication> authenticationMono = exchange.getPrincipal();
        Mono<OpenUserDetails> authentication = authenticationMono
                .map(Authentication::getPrincipal)
                .cast(OpenUserDetails.class);
        Map<String, String> map = Maps.newHashMap();
        authentication.subscribe(user -> map.put(key, JSONObject.toJSONString(user)));
        return map.get(key);
    }
}