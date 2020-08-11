package com.smart4y.cloud.base.gateway.interfaces.rest;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.gateway.application.GatewayRouteService;
import com.smart4y.cloud.base.gateway.domain.model.GatewayRoute;
import com.smart4y.cloud.base.gateway.interfaces.dtos.AddRouteCommand;
import com.smart4y.cloud.base.gateway.interfaces.dtos.DeleteRouteCommand;
import com.smart4y.cloud.base.gateway.interfaces.dtos.UpdateRouteCommand;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayRouteConverter;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayRouteQuery;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayRouteVO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import com.smart4y.cloud.core.toolkit.base.StringHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 网关智能路由
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Api(tags = "网关智能路由")
@RestController
public class GatewayRouteController {

    @Autowired
    private GatewayRouteConverter gatewayRouteConverter;
    @Autowired
    private GatewayRouteService gatewayRouteService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页路由列表
     */
    @ApiOperation(value = "获取分页路由列表", notes = "获取分页路由列表")
    @GetMapping("/gateway/route")
    public ResultMessage<Page<GatewayRouteVO>> getRouteListPage(GatewayRouteQuery query) {
        PageInfo<GatewayRoute> pageInfo = gatewayRouteService.findListPage(query);
        Page<GatewayRouteVO> result = gatewayRouteConverter.convertPage(pageInfo);
        return ResultMessage.ok(result);
    }

    /**
     * 获取路由
     */
    @ApiOperation(value = "获取路由", notes = "获取路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeId", required = true, value = "路由ID", paramType = "path"),
    })
    @GetMapping("/gateway/route/{routeId}/info")
    public ResultMessage<GatewayRouteVO> getRoute(@PathVariable("routeId") Long routeId) {
        GatewayRoute route = gatewayRouteService.getRoute(routeId);
        GatewayRouteVO result = gatewayRouteConverter.convert(route);
        return ResultMessage.ok(result);
    }

    /**
     * 添加路由
     */
    @ApiOperation(value = "添加路由", notes = "添加路由")
    @PostMapping("/gateway/route/add")
    public ResultMessage<Long> addRoute(@RequestBody AddRouteCommand command) {
        GatewayRoute route = new GatewayRoute();
        route.setRoutePath(command.getPath());
        route.setRouteServiceId(command.getServiceId());
        route.setRouteUrl(command.getUrl());
        route.setRouteRetryable(command.getRetryable());
        route.setRouteStripPrefix(command.getStripPrefix());
        route.setRouteState(1 == command.getStatus() ? "10" : "20");
        route.setRouteName(command.getRouteName());
        route.setRouteDesc(command.getRouteDesc());
        if (route.getRouteUrl() != null && StringHelper.isNotEmpty(route.getRouteUrl())) {
            route.setRouteServiceId(null);
        }
        long routeId = gatewayRouteService.addRoute(route);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok(routeId);
    }

    /**
     * 编辑路由
     */
    @ApiOperation(value = "编辑路由", notes = "编辑路由")
    @PostMapping("/gateway/route/update")
    public ResultMessage<Void> updateRoute(@RequestBody UpdateRouteCommand command) {
        GatewayRoute route = new GatewayRoute();
        route.setRouteId(command.getRouteId());
        route.setRouteName(command.getRouteName());
        route.setRouteDesc(command.getRouteDesc());
        route.setRoutePath(command.getPath());
        route.setRouteServiceId(command.getServiceId());
        route.setRouteUrl(command.getUrl());
        route.setRouteStripPrefix(command.getStripPrefix());
        route.setRouteRetryable(command.getRetryable());
        route.setRouteState(1 == command.getStatus() ? "10" : "20");
        if (route.getRouteUrl() != null && StringHelper.isNotEmpty(route.getRouteUrl())) {
            route.setRouteServiceId(null);
        }
        gatewayRouteService.updateRoute(route);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 移除路由
     */
    @ApiOperation(value = "移除路由", notes = "移除路由")
    @PostMapping("/gateway/route/remove")
    public ResultMessage<Void> removeRoute(@RequestBody DeleteRouteCommand command) {
        gatewayRouteService.removeRoute(command.getRouteId());
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}