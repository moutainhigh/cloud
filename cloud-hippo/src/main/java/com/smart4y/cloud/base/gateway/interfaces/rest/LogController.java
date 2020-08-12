package com.smart4y.cloud.base.gateway.interfaces.rest;

import com.smart4y.cloud.base.gateway.application.LogApplicationService;
import com.smart4y.cloud.base.gateway.domain.model.GatewayLog;
import com.smart4y.cloud.base.gateway.interfaces.dtos.log.LogPageQuery;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关智能路由
 *
 * @author Youtao on 2019-09-05.
 */
@Slf4j
@RestController
@Api(tags = {"访问控制 - 日志"})
public class LogController extends BaseGatewayController {

    @Autowired
    private LogApplicationService logApplicationService;

    @GetMapping("/logs/page")
    @ApiOperation(value = "日志:分页")
    public ResultMessage<Page<GatewayLog>> getLogsPage(LogPageQuery query) {
        Page<GatewayLog> result = logApplicationService.getLogsPage(query);
        return ResultMessage.ok(result);
    }
}