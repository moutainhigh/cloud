package com.smart4y.cloud.spider.infrastructure.exception;

import com.smart4y.cloud.core.exception.OpenSignatureException;
import com.smart4y.cloud.spider.application.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关权限异常处理（记录日志）
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class JsonSignatureDeniedHandler extends DeniedHandler {

    public JsonSignatureDeniedHandler(AccessLogService accessLogAdapter) {
        super(accessLogAdapter);
    }

    public Mono<Void> handle(ServerWebExchange exchange, OpenSignatureException e) {
        return super.handle(exchange, e);
    }
}