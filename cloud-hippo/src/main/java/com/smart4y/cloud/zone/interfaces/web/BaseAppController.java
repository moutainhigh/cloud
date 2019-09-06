package com.smart4y.cloud.zone.interfaces.web;

import com.smart4y.cloud.core.ResultBody;
import com.smart4y.cloud.core.application.dto.AppDTO;
import com.smart4y.cloud.core.application.dto.OpenClientDetailsDTO;
import com.smart4y.cloud.hippo.interfaces.feign.BaseAppFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统应用信息服务
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
@RestController
@RequestMapping
@Api(tags = "系统应用管理")
public class BaseAppController implements BaseAppFeign {

    @Override
    @GetMapping("/app/{appId}/info")
    @ApiOperation(value = "获取应用详情", notes = "获取应用详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    public ResultBody<AppDTO> getApp(@PathVariable("appId") String appId) {
        // TODO 待开发补充内容
        return ResultBody.ok().data(new AppDTO());
    }

    @Override
    @GetMapping("/app/client/{clientId}/info")
    @ApiOperation(value = "获取应用开发配置信息", notes = "获取应用开发配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    public ResultBody<OpenClientDetailsDTO> getAppClientInfo(@PathVariable("clientId") String clientId) {
        // TODO 待开发补充内容
        return ResultBody.ok().data(new OpenClientDetailsDTO());
    }
}