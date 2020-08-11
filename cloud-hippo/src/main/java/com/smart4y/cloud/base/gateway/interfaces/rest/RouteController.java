package com.smart4y.cloud.base.gateway.interfaces.rest;

import com.smart4y.cloud.base.access.domain.model.RbacOperation;
import com.smart4y.cloud.base.access.interfaces.dtos.operation.RbacOperationPageQuery;
import com.smart4y.cloud.base.gateway.application.RouteApplicationService;
import com.smart4y.cloud.base.gateway.domain.model.GatewayRoute;
import com.smart4y.cloud.base.gateway.interfaces.dtos.route.RoutePageQuery;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao on 2020/8/11 17:07
 */
@Slf4j
@RestController
@Api(tags = {"访问控制 - 路由"})
public class RouteController extends BaseGatewayController {

    @Autowired
    private RouteApplicationService routeApplicationService;

    @GetMapping("/routes/page")
    @ApiOperation(value = "操作:分页")
    public ResultMessage<Page<GatewayRoute>> getRoutesPage(RoutePageQuery query) {
        Page<GatewayRoute> result = routeApplicationService.getRoutesPage(query);
        return ok(result);
    }
}