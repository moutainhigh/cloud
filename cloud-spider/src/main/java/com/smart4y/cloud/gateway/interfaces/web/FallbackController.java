package com.smart4y.cloud.gateway.interfaces.web;

import com.smart4y.cloud.core.domain.ResultBody;
import com.smart4y.cloud.core.infrastructure.constants.ErrorCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 响应超时熔断处理器
 *
 * @author Youtao
 *         Created by youtao on 2019-09-08.
 */
@RestController
public class FallbackController {

    @RequestMapping("/fallback")
    public Mono<ResultBody> fallback() {
        return Mono.just(ResultBody.failed().code(ErrorCode.GATEWAY_TIMEOUT.getCode()).msg("访问超时，请稍后再试!"));
    }
}