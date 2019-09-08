package com.smart4y.cloud.gateway.infrastructure.filter;

import com.smart4y.cloud.core.infrastructure.interceptor.FeignRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * 请求前缀（增加请求时间） 过滤器
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class PreRequestFilter implements WebFilter {

    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        // 添加自定义请求头
        String rid = UUID.randomUUID().toString();
        ServerHttpRequest request = exchange.getRequest()
                .mutate().header(FeignRequestInterceptor.X_REQUEST_ID, rid)
                .build();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set(FeignRequestInterceptor.X_REQUEST_ID, rid);
        // 将现在的request 变成 change对象
        ServerWebExchange build = exchange.mutate().request(request).response(response).build();
        // 添加请求时间
        build.getAttributes().put("requestTime", new Date());
        return chain.filter(build);
    }
}