package com.smart4y.cloud.gateway.infrastructure.exception;

import com.smart4y.cloud.gateway.application.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关权限异常处理（记录日志）
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class JsonAccessDeniedHandler extends DeniedHandler implements ServerAccessDeniedHandler {


    public JsonAccessDeniedHandler(AccessLogService accessLogAdapter) {
        super(accessLogAdapter);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
        return super.handle(exchange, e);
    }
}