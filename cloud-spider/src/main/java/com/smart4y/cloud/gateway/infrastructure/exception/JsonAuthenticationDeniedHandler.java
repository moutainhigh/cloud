package com.smart4y.cloud.gateway.infrastructure.exception;

import com.smart4y.cloud.gateway.infrastructure.service.AccessLogService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关认证异常处理（记录日志）
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class JsonAuthenticationDeniedHandler extends DeniedHandler implements ServerAuthenticationEntryPoint {

    public JsonAuthenticationDeniedHandler(AccessLogService accessLogAdapter) {
        super(accessLogAdapter);
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        return super.handle(exchange, e);
    }
}