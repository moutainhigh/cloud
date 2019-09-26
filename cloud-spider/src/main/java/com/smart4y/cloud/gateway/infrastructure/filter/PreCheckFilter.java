package com.smart4y.cloud.gateway.infrastructure.filter;

import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;
import com.smart4y.cloud.core.infrastructure.constants.ErrorCode;
import com.smart4y.cloud.gateway.infrastructure.exception.JsonAccessDeniedHandler;
import com.smart4y.cloud.gateway.infrastructure.security.AccessManager;
import com.smart4y.cloud.gateway.infrastructure.toolkit.ReactiveWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 访问验证前置 过滤器
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class PreCheckFilter implements WebFilter {

    private final JsonAccessDeniedHandler accessDeniedHandler;

    private final AccessManager accessManager;

    public PreCheckFilter(AccessManager accessManager, JsonAccessDeniedHandler accessDeniedHandler) {
        this.accessManager = accessManager;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //ServerHttpResponse response = exchange.getResponse();
        String requestPath = request.getURI().getPath();
        String remoteIpAddress = ReactiveWebUtils.getRemoteAddress(exchange);
        String origin = request.getHeaders().getOrigin();
        AuthorityResourceDTO resource = accessManager.getResource(requestPath);
        if (resource != null) {
            if ("0".equals(resource.getIsOpen().toString())) {
                // 未公开
                return accessDeniedHandler.handle(exchange, new AccessDeniedException(ErrorCode.ACCESS_DENIED_NOT_OPEN.getMessage()));
            }
            if ("0".equals(resource.getStatus().toString())) {
                // 禁用
                return accessDeniedHandler.handle(exchange, new AccessDeniedException(ErrorCode.ACCESS_DENIED_DISABLED.getMessage()));
            } else if ("2".equals(resource.getStatus().toString())) {
                // 维护中
                return accessDeniedHandler.handle(exchange, new AccessDeniedException(ErrorCode.ACCESS_DENIED_UPDATING.getMessage()));
            }
        }
        // 1 IP黑名单检测
        boolean deny = accessManager.matchIpOrOriginBlacklist(requestPath, remoteIpAddress, origin);
        if (deny) {
            // 拒绝
            return accessDeniedHandler.handle(exchange, new AccessDeniedException(ErrorCode.ACCESS_DENIED_BLACK_LIMITED.getMessage()));
        }
        // 3 IP白名单检测
        Boolean[] matchIpWhiteListResult = accessManager.matchIpOrOriginWhiteList(requestPath, remoteIpAddress, origin);
        boolean hasWhiteList = matchIpWhiteListResult[0];
        boolean allow = matchIpWhiteListResult[1];
        if (hasWhiteList) {
            // 接口存在白名单限制
            if (!allow) {
                // IP白名单检测通过,拒绝
                return accessDeniedHandler.handle(exchange, new AccessDeniedException(ErrorCode.ACCESS_DENIED_WHITE_LIMITED.getMessage()));
            }
        }
        return chain.filter(exchange);
    }
}