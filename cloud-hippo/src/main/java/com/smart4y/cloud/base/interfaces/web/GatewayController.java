package com.smart4y.cloud.base.interfaces.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.smart4y.cloud.base.application.GatewayIpLimitService;
import com.smart4y.cloud.base.application.GatewayRateLimitService;
import com.smart4y.cloud.base.application.GatewayRouteService;
import com.smart4y.cloud.base.domain.model.GatewayRoute;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import com.smart4y.cloud.core.domain.ResultEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 网关接口
 *
 * @author: liuyadu
 * @date: 2019/3/12 15:12
 * @description:
 */
@Api(tags = "网关对外接口")
@RestController
public class GatewayController {

    @Autowired
    private GatewayIpLimitService gatewayIpLimitService;
    @Autowired
    private GatewayRateLimitService gatewayRateLimitService;
    @Autowired
    private GatewayRouteService gatewayRouteService;

    @ApiOperation(value = "获取服务列表", notes = "获取服务列表")
    @GetMapping("/gateway/service/list")
    public ResultEntity<List<Map<String, Object>>> getServiceList() {
        List<Map<String, Object>> services = Lists.newArrayList();
        List<GatewayRoute> routes = gatewayRouteService.findRouteList();
        if (routes != null && routes.size() > 0) {
            routes.forEach(route -> {
                Map<String, Object> service = Maps.newHashMap();
                service.put("serviceId", route.getRouteName());
                service.put("serviceName", route.getRouteDesc());
                services.add(service);
            });
        }
        return ResultEntity.ok(services);
    }

    /**
     * 获取接口黑名单列表
     */
    @ApiOperation(value = "获取接口黑名单列表", notes = "仅限内部调用")
    @GetMapping("/gateway/api/blackList")
    public ResultEntity<List<IpLimitApiDTO>> getApiBlackList() {
        return ResultEntity.ok(gatewayIpLimitService.findBlackList());
    }

    /**
     * 获取接口白名单列表
     *
     * @return
     */
    @ApiOperation(value = "获取接口白名单列表", notes = "仅限内部调用")
    @GetMapping("/gateway/api/whiteList")
    public ResultEntity<List<IpLimitApiDTO>> getApiWhiteList() {
        return ResultEntity.ok(gatewayIpLimitService.findWhiteList());
    }

    /**
     * 获取限流列表
     *
     * @return
     */
    @ApiOperation(value = "获取限流列表", notes = "仅限内部调用")
    @GetMapping("/gateway/api/rateLimit")
    public ResultEntity<List<RateLimitApiDTO>> getApiRateLimitList() {
        return ResultEntity.ok(gatewayRateLimitService.findRateLimitApiList());
    }

    /**
     * 获取路由列表
     *
     * @return
     */
    @ApiOperation(value = "获取路由列表", notes = "仅限内部调用")
    @GetMapping("/gateway/api/route")
    public ResultEntity<List<GatewayRoute>> getApiRouteList() {
        return ResultEntity.ok(gatewayRouteService.findRouteList());
    }
}