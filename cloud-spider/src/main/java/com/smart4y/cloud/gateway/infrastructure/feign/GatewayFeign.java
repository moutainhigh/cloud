package com.smart4y.cloud.gateway.infrastructure.feign;

import com.smart4y.cloud.core.domain.ResultEntity;
import com.smart4y.cloud.core.application.dto.GatewayRouteDTO;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 网关对外信息服务
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVER)
public interface GatewayFeign {

    /**
     * 获取接口黑名单列表
     */
    @GetMapping("/gateway/api/blackList")
    ResultEntity<List<IpLimitApiDTO>> getApiBlackList();

    /**
     * 获取接口白名单列表
     */
    @GetMapping("/gateway/api/whiteList")
    ResultEntity<List<IpLimitApiDTO>> getApiWhiteList();

    /**
     * 获取限流列表
     */
    @GetMapping("/gateway/api/rateLimit")
    ResultEntity<List<RateLimitApiDTO>> getApiRateLimitList();

    /**
     * 获取路由列表
     */
    @GetMapping("/gateway/api/route")
    ResultEntity<List<GatewayRouteDTO>> getApiRouteList();
}