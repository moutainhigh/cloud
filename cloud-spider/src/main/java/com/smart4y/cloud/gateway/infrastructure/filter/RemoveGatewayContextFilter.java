package com.smart4y.cloud.gateway.infrastructure.filter;

import com.smart4y.cloud.core.interceptor.FeignRequestInterceptor;
import com.smart4y.cloud.gateway.domain.GatewayContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 移除GatewayContext 过滤器
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class RemoveGatewayContextFilter implements WebFilter, Ordered {

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {

        List<String> traceId = exchange.getRequest().getHeaders().get(FeignRequestInterceptor.X_REQUEST_ID);
        String format = String.format(">>>>> >>>>> >>>>> %s, traceId=%s, path=%s",
                this.getClass().getSimpleName(), traceId, (exchange.getRequest().getPath()  + exchange.getRequest().getMethodValue()));
        log.info(format);

        return chain
                .filter(exchange)
                .doFinally(s -> exchange.getAttributes().remove(GatewayContext.CACHE_GATEWAY_CONTEXT));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}