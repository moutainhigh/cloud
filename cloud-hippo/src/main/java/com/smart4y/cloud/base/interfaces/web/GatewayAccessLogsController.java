package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.GatewayAccessLogsService;
import com.smart4y.cloud.base.domain.model.GatewayAccessLogs;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.domain.ResultEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 网关智能路由
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Api(tags = "网关访问日志")
@RestController
public class GatewayAccessLogsController {

    @Autowired
    private GatewayAccessLogsService gatewayAccessLogsService;

    /**
     * 获取分页列表
     */
    @ApiOperation(value = "获取分页访问日志列表", notes = "获取分页访问日志列表")
    @GetMapping("/gateway/access/logs")
    public ResultEntity<IPage<GatewayAccessLogs>> getAccessLogListPage(@RequestParam(required = false) Map map) {
        IPage<GatewayAccessLogs> listPage = gatewayAccessLogsService.findListPage(new PageParams(map));
        return ResultEntity.ok(listPage);
    }
}