package com.smart4y.cloud.gateway.infrastructure.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.smart4y.cloud.core.domain.OpenUserDetails;
import com.smart4y.cloud.core.infrastructure.constants.QueueConstants;
import com.smart4y.cloud.gateway.infrastructure.filter.context.GatewayContext;
import com.smart4y.cloud.gateway.infrastructure.toolkit.ReactiveWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.*;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@Component
public class JdbcAccessLogService implements AccessLogService {

    @Value("${spring.application.name}")
    private String defaultServiceId;

    private final AmqpTemplate amqpTemplate;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    public JdbcAccessLogService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    /**
     * 不记录日志的请求列表
     */
    private List<String> ignores = Arrays.asList(
            "/**/oauth/check_token/**",
            "/**/gateway/access/logs/**",
            "/webjars/**");

    /**
     * 是否忽略记录日志
     */
    private boolean ignore(String requestPath) {
        return ignores.stream()
                .anyMatch(path -> antPathMatcher.match(path, requestPath));
    }

    @Override
    public void sendLog(ServerWebExchange exchange, Exception ex) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String requestPath = request.getURI().getPath();
        if (ignore(requestPath)) {
            return;
        }

        String method = request.getMethodValue();
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        String userAgent = headers.get(HttpHeaders.USER_AGENT);
        String ip = ReactiveWebUtils.getRemoteAddress(exchange);
        Object requestTime = exchange.getAttribute("requestTime");
        String serviceId = getServiceId(exchange);
        Map data = getParams(exchange);
        String error = getErrorMessage(ex);
        int httpStatus = Objects.requireNonNull(response.getStatusCode()).value();
        String authentication = getAuthentication(exchange);

        Map<String, Object> map = Maps.newHashMap();
        map.put("requestTime", requestTime);
        map.put("serviceId", serviceId);
        map.put("httpStatus", httpStatus);
        map.put("headers", JSONObject.toJSON(headers));
        map.put("path", requestPath);
        map.put("params", JSONObject.toJSON(data));
        map.put("ip", ip);
        map.put("method", method);
        map.put("userAgent", userAgent);
        map.put("responseTime", new Date());
        map.put("error", error);
        map.put("authentication", authentication);
        try {
            amqpTemplate.convertAndSend(QueueConstants.QUEUE_ACCESS_LOGS, map);
        } catch (Exception e) {
            log.error("Access log save error {}", e.getMessage(), e);
        }
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
    private Map getParams(ServerWebExchange exchange) {
        Map data = Maps.newHashMap();
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null) {
            data = gatewayContext.getAllRequestData().toSingleValueMap();
        }
        return data;
    }
}