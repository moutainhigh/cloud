package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.GatewayRouteService;
import com.smart4y.cloud.base.domain.model.GatewayRoute;
import com.smart4y.cloud.base.interfaces.converter.GatewayRouteConverter;
import com.smart4y.cloud.base.interfaces.valueobject.query.GatewayRouteQuery;
import com.smart4y.cloud.base.interfaces.valueobject.vo.GatewayRouteVO;
import com.smart4y.cloud.core.domain.page.Page;
import com.smart4y.cloud.core.domain.ResultEntity;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtil;
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
 *         Created by youtao on 2019-09-05.
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
    public ResultEntity<Page<GatewayRouteVO>> getRouteListPage(GatewayRouteQuery query) {
        PageInfo<GatewayRoute> pageInfo = gatewayRouteService.findListPage(query);
        Page<GatewayRouteVO> result = gatewayRouteConverter.convertPage(pageInfo);
        return ResultEntity.ok(result);
    }

    /**
     * 获取路由
     */
    @ApiOperation(value = "获取路由", notes = "获取路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeId", required = true, value = "路由ID", paramType = "path"),
    })
    @GetMapping("/gateway/route/{routeId}/info")
    public ResultEntity<GatewayRouteVO> getRoute(@PathVariable("routeId") Long routeId) {
        GatewayRoute route = gatewayRouteService.getRoute(routeId);
        GatewayRouteVO result = gatewayRouteConverter.convert(route);
        return ResultEntity.ok(result);
    }

    /**
     * 添加路由
     *
     * @param path        路径表达式
     * @param routeName   描述
     * @param serviceId   服务名方转发
     * @param url         地址转发
     * @param stripPrefix 忽略前缀
     * @param retryable   支持重试
     * @param status      是否启用
     */
    @ApiOperation(value = "添加路由", notes = "添加路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", required = true, value = "路径表达式", paramType = "form"),
            @ApiImplicitParam(name = "routeName", required = true, value = "路由标识", paramType = "form"),
            @ApiImplicitParam(name = "routeDesc", required = true, value = "路由名称", paramType = "form"),
            @ApiImplicitParam(name = "serviceId", value = "服务名方转发", paramType = "form"),
            @ApiImplicitParam(name = "url", value = "地址转发", paramType = "form"),
            @ApiImplicitParam(name = "stripPrefix", allowableValues = "0,1", defaultValue = "1", value = "忽略前缀", paramType = "form"),
            @ApiImplicitParam(name = "retryable", allowableValues = "0,1", defaultValue = "0", value = "支持重试", paramType = "form"),
            @ApiImplicitParam(name = "status", allowableValues = "0,1", defaultValue = "1", value = "是否启用", paramType = "form")
    })
    @PostMapping("/gateway/route/add")
    public ResultEntity<Long> addRoute(
            @RequestParam(value = "routeName", defaultValue = "") String routeName,
            @RequestParam(value = "routeDesc", defaultValue = "") String routeDesc,
            @RequestParam(value = "path") String path,
            @RequestParam(value = "serviceId", required = false) String serviceId,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "stripPrefix", required = false, defaultValue = "1") Integer stripPrefix,
            @RequestParam(value = "retryable", required = false, defaultValue = "0") Integer retryable,
            @RequestParam(value = "status", defaultValue = "1") Integer status
    ) {
        GatewayRoute route = new GatewayRoute();
        route.setPath(path);
        route.setServiceId(serviceId);
        route.setUrl(url);
        route.setRetryable(retryable);
        route.setStripPrefix(stripPrefix);
        route.setStatus(status);
        route.setRouteName(routeName);
        route.setRouteDesc(routeDesc);
        if (route.getUrl() != null && StringUtil.isNotEmpty(route.getUrl())) {
            route.setServiceId(null);
        }
        long routeId = gatewayRouteService.addRoute(route);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultEntity.ok(routeId);
    }

    /**
     * 编辑路由
     *
     * @param routeId     路由ID
     * @param path        路径表达式
     * @param serviceId   服务名方转发
     * @param url         地址转发
     * @param stripPrefix 忽略前缀
     * @param retryable   支持重试
     * @param status      是否启用
     * @param routeName   描述
     */
    @ApiOperation(value = "编辑路由", notes = "编辑路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeId", required = true, value = "路由Id", paramType = "form"),
            @ApiImplicitParam(name = "routeName", required = true, value = "路由标识", paramType = "form"),
            @ApiImplicitParam(name = "routeDesc", required = true, value = "路由名称", paramType = "form"),
            @ApiImplicitParam(name = "path", required = true, value = "路径表达式", paramType = "form"),
            @ApiImplicitParam(name = "serviceId", value = "服务名方转发", paramType = "form"),
            @ApiImplicitParam(name = "url", value = "地址转发", paramType = "form"),
            @ApiImplicitParam(name = "stripPrefix", allowableValues = "0,1", defaultValue = "1", value = "忽略前缀", paramType = "form"),
            @ApiImplicitParam(name = "retryable", allowableValues = "0,1", defaultValue = "0", value = "支持重试", paramType = "form"),
            @ApiImplicitParam(name = "status", allowableValues = "0,1", defaultValue = "1", value = "是否启用", paramType = "form")
    })
    @PostMapping("/gateway/route/update")
    public ResultEntity updateRoute(
            @RequestParam("routeId") Long routeId,
            @RequestParam(value = "routeName", defaultValue = "") String routeName,
            @RequestParam(value = "routeDesc", defaultValue = "") String routeDesc,
            @RequestParam(value = "path") String path,
            @RequestParam(value = "serviceId", required = false) String serviceId,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "stripPrefix", required = false, defaultValue = "1") Integer stripPrefix,
            @RequestParam(value = "retryable", required = false, defaultValue = "0") Integer retryable,
            @RequestParam(value = "status", defaultValue = "1") Integer status
    ) {
        GatewayRoute route = new GatewayRoute();
        route.setRouteId(routeId);
        route.setPath(path);
        route.setServiceId(serviceId);
        route.setUrl(url);
        route.setRetryable(retryable);
        route.setStripPrefix(stripPrefix);
        route.setStatus(status);
        route.setRouteName(routeName);
        route.setRouteDesc(routeDesc);
        if (route.getUrl() != null && StringUtil.isNotEmpty(route.getUrl())) {
            route.setServiceId(null);
        }
        gatewayRouteService.updateRoute(route);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultEntity.ok();
    }

    /**
     * 移除路由
     */
    @ApiOperation(value = "移除路由", notes = "移除路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeId", required = true, value = "routeId", paramType = "form"),
    })
    @PostMapping("/gateway/route/remove")
    public ResultEntity removeRoute(
            @RequestParam("routeId") Long routeId
    ) {
        gatewayRouteService.removeRoute(routeId);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultEntity.ok();
    }
}