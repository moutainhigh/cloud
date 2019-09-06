package com.smart4y.cloud.gateway.infrastructure.service.feign;

import com.smart4y.cloud.core.domain.model.GatewayRoute;
import com.smart4y.cloud.core.domain.model.IpLimitApi;
import com.smart4y.cloud.core.domain.model.RateLimitApi;
import com.smart4y.cloud.core.ResultBody;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:49
 * @description:
 */
@Component
public class GatewayServiceClient {

    /**
     * 获取接口黑名单列表
     */
    @GetMapping("/gateway/api/blackList")
    public ResultBody<List<IpLimitApi>> getApiBlackList() {
        // TODO
        List<IpLimitApi> resources = Collections.emptyList();
        return new ResultBody<>().data(resources);
    }

    /**
     * 获取接口白名单列表
     */
    @GetMapping("/gateway/api/whiteList")
    public ResultBody<List<IpLimitApi>> getApiWhiteList() {
        // TODO
        List<IpLimitApi> resources = Collections.emptyList();
        return new ResultBody<>().data(resources);
    }

    /**
     * 获取限流列表
     */
    @GetMapping("/gateway/api/rateLimit")
    public ResultBody<List<RateLimitApi>> getApiRateLimitList() {
        // TODO
        List<RateLimitApi> resources = Collections.emptyList();
        return new ResultBody<>().data(resources);
    }

    /**
     * 获取路由列表
     */
    @GetMapping("/gateway/api/route")
    public ResultBody<List<GatewayRoute>> getApiRouteList() {
        // TODO
        List<GatewayRoute> resources = Collections.emptyList();
        return new ResultBody<>().data(resources);
    }
}