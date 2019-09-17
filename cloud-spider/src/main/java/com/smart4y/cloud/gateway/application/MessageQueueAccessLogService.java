package com.smart4y.cloud.gateway.application;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.smart4y.cloud.core.domain.event.LogAccessedEvent;
import com.smart4y.cloud.core.infrastructure.constants.QueueConstants;
import com.smart4y.cloud.core.infrastructure.security.OpenUserDetails;
import com.smart4y.cloud.gateway.domain.GatewayContext;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * 网关日志记录 实现类
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@Component
public class MessageQueueAccessLogService implements AccessLogService {

    private final AmqpTemplate amqpTemplate;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Value("${spring.application.name}")
    private String defaultServiceId;
    /**
     * 不记录日志的请求列表
     */
    private List<String> ignores = Arrays.asList(
            "/**/oauth/check_token/**",
            "/**/gateway/access/logs/**",
            "/webjars/**");

    @Autowired
    public MessageQueueAccessLogService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

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
        LocalDateTime requestTime = Objects
                .requireNonNull((Date) exchange.getAttribute("requestTime"))
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String serviceId = getServiceId(exchange);
        Map data = getParams(exchange);
        String error = getErrorMessage(ex);
        int httpStatus = Objects.requireNonNull(response.getStatusCode()).value();
        String authentication = getAuthentication(exchange);

        LogAccessedEvent event = new LogAccessedEvent()
                .setRequestTime(requestTime)
                .setServiceId(serviceId)
                .setHttpStatus(String.valueOf(httpStatus))
                .setHeaders(JSONObject.toJSONString(headers))
                .setPath(requestPath)
                .setParams(JSONObject.toJSONString(data))
                .setIp(ip)
                .setMethod(method)
                .setUserAgent(userAgent)
                .setResponseTime(LocalDateTime.now())
                .setError(error)
                .setAuthentication(authentication);
        try {
            amqpTemplate.convertAndSend(QueueConstants.QUEUE_ACCESS_LOGS, event);
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