package com.smart4y.cloud.zone.interfaces.web;

import com.smart4y.cloud.core.ResultBody;
import com.smart4y.cloud.core.application.dto.GatewayRouteDTO;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import com.smart4y.cloud.hippo.interfaces.feign.GatewayFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 网关对外管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
@RestController
@Api(tags = "网关对外接口")
public class GatewayController implements GatewayFeign {

    @Override
    @GetMapping("/gateway/api/blackList")
    @ApiOperation(value = "获取接口黑名单列表", notes = "仅限内部调用")
    public ResultBody<List<IpLimitApiDTO>> getApiBlackList() {
        // TODO 待开发补充内容
        return ResultBody.ok().data(Collections.emptyList());
    }

    @Override
    @GetMapping("/gateway/api/whiteList")
    @ApiOperation(value = "获取接口白名单列表", notes = "仅限内部调用")
    public ResultBody<List<IpLimitApiDTO>> getApiWhiteList() {
        // TODO 待开发补充内容
        return ResultBody.ok().data(Collections.emptyList());
    }

    @Override
    @GetMapping("/gateway/api/rateLimit")
    @ApiOperation(value = "获取限流列表", notes = "仅限内部调用")
    public ResultBody<List<RateLimitApiDTO>> getApiRateLimitList() {
        // TODO 待开发补充内容
        return ResultBody.ok().data(Collections.emptyList());
    }

    @Override
    @GetMapping("/gateway/api/route")
    @ApiOperation(value = "获取路由列表", notes = "仅限内部调用")
    public ResultBody<List<GatewayRouteDTO>> getApiRouteList() {
        // TODO 待开发补充内容
        return ResultBody.ok().data(Collections.emptyList());
    }
}