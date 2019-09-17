package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.AppService;
import com.smart4y.cloud.core.application.dto.AppDTO;
import com.smart4y.cloud.core.application.dto.OpenClientDetailsDTO;
import com.smart4y.cloud.core.domain.ResultBody;
import com.smart4y.cloud.core.infrastructure.security.OpenClientDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BaseAppController {

    private final AppService appService;

    @Autowired
    public BaseAppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/app/{appId}/info")
    @ApiOperation(value = "获取应用详情", notes = "获取应用详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    public ResultBody<AppDTO> getApp(@PathVariable("appId") String appId) {
        AppDTO result = appService.getAppInfo(appId);
        return ResultBody.ok().data(result);
    }

    @GetMapping("/app/client/{clientId}/info")
    @ApiOperation(value = "获取应用开发配置信息", notes = "获取应用开发配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    public ResultBody<OpenClientDetailsDTO> getAppClientInfo(@PathVariable("clientId") String clientId) {
        OpenClientDetails result = appService.getAppClientInfo(clientId);
        return ResultBody.ok().data(result);
    }
}