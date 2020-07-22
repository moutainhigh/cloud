package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.GatewayIpLimitService;
import com.smart4y.cloud.base.application.GatewayRateLimitService;
import com.smart4y.cloud.base.application.GatewayRouteService;
import com.smart4y.cloud.base.domain.model.GatewayRoute;
import com.smart4y.cloud.base.interfaces.converter.GatewayRouteConverter;
import com.smart4y.cloud.base.interfaces.valueobject.vo.GatewayRouteVO;
import com.smart4y.cloud.base.interfaces.valueobject.vo.ServiceVO;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.interfaces.IpLimitApiDTO;
import com.smart4y.cloud.core.interfaces.RateLimitApiDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 网关接口
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Api(tags = "网关对外接口")
@RestController
public class GatewayController {


    @Autowired
    private GatewayRouteConverter gatewayRouteConverter;
    @Autowired
    private GatewayIpLimitService gatewayIpLimitService;
    @Autowired
    private GatewayRateLimitService gatewayRateLimitService;
    @Autowired
    private GatewayRouteService gatewayRouteService;

    /**
     * 获取路由列表
     */
    @ApiOperation(value = "获取路由列表", notes = "仅限内部调用")
    @GetMapping("/gateway/api/route")
    public ResultMessage<List<GatewayRouteVO>> getApiRouteList() {
        List<GatewayRoute> list = gatewayRouteService.findRouteList();
        List<GatewayRouteVO> result = gatewayRouteConverter.convertList(list);
        return ResultMessage.ok(result);
    }

    @ApiOperation(value = "获取服务列表", notes = "获取服务列表")
    @GetMapping("/gateway/service/list")
    public ResultMessage<List<ServiceVO>> getServiceList() {
        List<GatewayRoute> routes = gatewayRouteService.findRouteList();
        List<ServiceVO> services = routes.stream()
                .map(r -> new ServiceVO()
                        .setServiceId(r.getRouteName())
                        .setServiceDesc(r.getRouteDesc()))
                .collect(Collectors.toList());
        return ResultMessage.ok(services);
    }

    /**
     * 获取接口黑名单列表
     */
    @ApiOperation(value = "获取接口黑名单列表", notes = "仅限内部调用")
    @GetMapping("/gateway/api/blackList")
    public ResultMessage<List<IpLimitApiDTO>> getApiBlackList() {
        List<IpLimitApiDTO> result = gatewayIpLimitService.findBlackList();
        return ResultMessage.ok(result);
    }

    /**
     * 获取接口白名单列表
     */
    @ApiOperation(value = "获取接口白名单列表", notes = "仅限内部调用")
    @GetMapping("/gateway/api/whiteList")
    public ResultMessage<List<IpLimitApiDTO>> getApiWhiteList() {
        List<IpLimitApiDTO> result = gatewayIpLimitService.findWhiteList();
        return ResultMessage.ok(result);
    }

    /**
     * 获取限流列表
     */
    @ApiOperation(value = "获取限流列表", notes = "仅限内部调用")
    @GetMapping("/gateway/api/rateLimit")
    public ResultMessage<List<RateLimitApiDTO>> getApiRateLimitList() {
        List<RateLimitApiDTO> result = gatewayRateLimitService.findRateLimitApiList();
        return ResultMessage.ok(result);
    }
}