package com.smart4y.cloud.gateway.infrastructure.service.feign;

import com.smart4y.cloud.core.application.dto.GatewayRouteDTO;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
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
    public ResultBody<List<IpLimitApiDTO>> getApiBlackList() {
        // TODO
        List<IpLimitApiDTO> resources = Collections.emptyList();
        return new ResultBody<>().data(resources);
    }

    /**
     * 获取接口白名单列表
     */
    @GetMapping("/gateway/api/whiteList")
    public ResultBody<List<IpLimitApiDTO>> getApiWhiteList() {
        // TODO
        List<IpLimitApiDTO> resources = Collections.emptyList();
        return new ResultBody<>().data(resources);
    }

    /**
     * 获取限流列表
     */
    @GetMapping("/gateway/api/rateLimit")
    public ResultBody<List<RateLimitApiDTO>> getApiRateLimitList() {
        // TODO
        List<RateLimitApiDTO> resources = Collections.emptyList();
        return new ResultBody<>().data(resources);
    }

    /**
     * 获取路由列表
     */
    @GetMapping("/gateway/api/route")
    public ResultBody<List<GatewayRouteDTO>> getApiRouteList() {
        // TODO
        List<GatewayRouteDTO> resources = Collections.emptyList();
        return new ResultBody<>().data(resources);
    }
}