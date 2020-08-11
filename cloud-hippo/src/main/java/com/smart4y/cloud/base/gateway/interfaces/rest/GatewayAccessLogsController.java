package com.smart4y.cloud.base.gateway.interfaces.rest;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.gateway.application.GatewayAccessLogsService;
import com.smart4y.cloud.base.gateway.domain.model.GatewayAccessLogs;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayAccessLogsConverter;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayAccessLogsQuery;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayAccessLogsVO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关智能路由
 *
 * @author Youtao on 2019-09-05.
 */
@RestController
@Api(tags = "网关访问日志")
public class GatewayAccessLogsController {

    @Autowired
    private GatewayAccessLogsConverter gatewayAccessLogsConverter;
    @Autowired
    private GatewayAccessLogsService gatewayAccessLogsService;

    /**
     * 获取分页列表
     */
    @GetMapping("/gateway/access/logs")
    @ApiOperation(value = "获取分页访问日志列表", notes = "获取分页访问日志列表")
    public ResultMessage<Page<GatewayAccessLogsVO>> getAccessLogListPage(GatewayAccessLogsQuery query) {
        PageInfo<GatewayAccessLogs> listPage = gatewayAccessLogsService.findListPage(query);
        Page<GatewayAccessLogsVO> result = gatewayAccessLogsConverter.convertPage(listPage);
        return ResultMessage.ok(result);
    }
}