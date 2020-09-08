package com.smart4y.cloud.spider.infrastructure.filter;

import com.smart4y.cloud.core.interceptor.FeignRequestInterceptor;
import com.smart4y.cloud.spider.application.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 日志 过滤器
 *
 * @author Youtao on 2019-09-05.
 */
@Slf4j
public class AccessLogFilter implements WebFilter {

    private final AccessLogService accessLogService;

    public AccessLogFilter(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        List<String> traceId = exchange.getRequest().getHeaders().get(FeignRequestInterceptor.X_REQUEST_ID);
        String format = String.format(">>>>> >>>>> >>>>> %s, traceId=%s, path=%s",
                this.getClass().getSimpleName(), traceId, (exchange.getRequest().getPath()  + exchange.getRequest().getMethodValue()));
        log.info(format);

        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        // probably should reuse buffers
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        // 释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        return bufferFactory.wrap(content);
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
        return chain
                .filter(exchange.mutate().response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> accessLogService.sendLog(exchange, null)));
    }
}