package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.gateway.interfaces.rest.BaseGatewayController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
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
/*
    @Autowired
    private LogService logService;

    @GetMapping("/logs/page")
    @ApiOperation(value = "日志:分页")
    public ResultMessage<Page<GatewayLog>> getLogsPage(LogPageQuery query) {
        Page<GatewayLog> result = logService.getPageLike(
                query.getPage(), query.getLimit(), query.getLogPath(), query.getLogIp(), query.getLogServiceId());
        return ResultMessage.ok(result);
    }*/
}