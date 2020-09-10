package com.smart4y.cloud.spider.infrastructure.exception;

import com.alibaba.fastjson.JSONObject;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.exception.global.ExceptionHelper;
import com.smart4y.cloud.spider.application.AccessLogService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * 异常处理（记录日志）
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
abstract class DeniedHandler {

    private final AccessLogService accessLogAdapter;

    DeniedHandler(AccessLogService accessLogAdapter) {
        this.accessLogAdapter = accessLogAdapter;
    }

    Mono<Void> handle(ServerWebExchange exchange, Exception e) {
        ResultMessage<Void> resultEntity = ExceptionHelper.resolveException(e);
        return Mono
                .defer(() -> Mono.just(exchange.getResponse()))
                .flatMap((response) -> {
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    DataBuffer buffer = dataBufferFactory.wrap(JSONObject.toJSONString(resultEntity).getBytes(Charset.defaultCharset()));
                    // 保存日志
                    accessLogAdapter.sendLog(exchange, e);
                    return response
                            .writeWith(Mono.just(buffer))
                            .doOnError((error) -> DataBufferUtils.release(buffer));
                });
    }
}