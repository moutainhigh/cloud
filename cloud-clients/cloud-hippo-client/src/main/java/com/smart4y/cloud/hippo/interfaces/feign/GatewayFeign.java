package com.smart4y.cloud.hippo.interfaces.feign;

import com.smart4y.cloud.core.ResultBody;
import com.smart4y.cloud.core.application.dto.GatewayRouteDTO;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 网关对外信息服务
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
public interface GatewayFeign {

    /**
     * 获取接口黑名单列表
     */
    @GetMapping("/gateway/api/blackList")
    ResultBody<List<IpLimitApiDTO>> getApiBlackList();

    /**
     * 获取接口白名单列表
     */
    @GetMapping("/gateway/api/whiteList")
    ResultBody<List<IpLimitApiDTO>> getApiWhiteList();

    /**
     * 获取限流列表
     */
    @GetMapping("/gateway/api/rateLimit")
    ResultBody<List<RateLimitApiDTO>> getApiRateLimitList();

    /**
     * 获取路由列表
     */
    @GetMapping("/gateway/api/route")
    ResultBody<List<GatewayRouteDTO>> getApiRouteList();
}