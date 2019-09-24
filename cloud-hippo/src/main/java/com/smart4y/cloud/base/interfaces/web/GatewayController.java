package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.GatewayService;
import com.smart4y.cloud.core.application.dto.GatewayRouteDTO;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import com.smart4y.cloud.core.domain.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO 网关对外管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
@RestController
@Api(tags = "网关对外接口")
public class GatewayController {

    private final GatewayService gatewayService;

    @Autowired
    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/gateway/api/blackList")
    @ApiOperation(value = "获取接口黑名单列表", notes = "仅限内部调用")
    public ResultBody<List<IpLimitApiDTO>> getApiBlackList() {
        List<IpLimitApiDTO> result = gatewayService.getBlackList();
        return ResultBody.ok().data(result);
    }

    @GetMapping("/gateway/api/whiteList")
    @ApiOperation(value = "获取接口白名单列表", notes = "仅限内部调用")
    public ResultBody<List<IpLimitApiDTO>> getApiWhiteList() {
        List<IpLimitApiDTO> result = gatewayService.getWhiteList();
        return ResultBody.ok().data(result);
    }

    @GetMapping("/gateway/api/rateLimit")
    @ApiOperation(value = "获取限流列表", notes = "仅限内部调用")
    public ResultBody<List<RateLimitApiDTO>> getApiRateLimitList() {
        List<RateLimitApiDTO> result = gatewayService.getRateLimitApiList();
        return ResultBody.ok().data(result);
    }

    @GetMapping("/gateway/api/route")
    @ApiOperation(value = "获取路由列表", notes = "仅限内部调用")
    public ResultBody<List<GatewayRouteDTO>> getApiRouteList() {
        List<GatewayRouteDTO> result = gatewayService.getRoutes();
        return ResultBody.ok().data(result);
    }
}