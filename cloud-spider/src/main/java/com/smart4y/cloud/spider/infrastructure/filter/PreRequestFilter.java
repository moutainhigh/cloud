package com.smart4y.cloud.spider.infrastructure.filter;

import com.smart4y.cloud.core.interceptor.FeignRequestInterceptor;
import com.smart4y.cloud.core.toolkit.Kit;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 请求前缀（增加请求时间） 过滤器
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class PreRequestFilter implements WebFilter {

    @NonNull
    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        // 添加自定义请求头
        String traceId = Kit.help().random().shortUuid();
        MDC.put(FeignRequestInterceptor.X_REQUEST_ID, traceId);

        String format = String.format(">>>>> >>>>> >>>>> %s, traceId=%s, path=%s",
                this.getClass().getSimpleName(), traceId, (exchange.getRequest().getPath() + exchange.getRequest().getMethodValue()));
        log.info(format);

        ServerHttpRequest request = exchange.getRequest()
                .mutate().header(FeignRequestInterceptor.X_REQUEST_ID, traceId)
                .build();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set(FeignRequestInterceptor.X_REQUEST_ID, traceId);
        // 将现在的request 变成 change对象
        ServerWebExchange build = exchange.mutate().request(request).response(response).build();
        // 添加请求时间
        build.getAttributes().put(FeignRequestInterceptor.X_REQUEST_TIME, LocalDateTime.now());
        return chain.filter(build);
    }
}