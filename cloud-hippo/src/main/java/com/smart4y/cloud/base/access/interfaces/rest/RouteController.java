package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.domain.service.RouteService;
import com.smart4y.cloud.base.access.interfaces.dtos.route.CreateRouteCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.route.ModifyRouteCommand;
import com.smart4y.cloud.base.gateway.domain.entity.GatewayRoute;
import com.smart4y.cloud.base.access.interfaces.dtos.route.RoutePageQuery;
import com.smart4y.cloud.base.gateway.interfaces.rest.BaseGatewayController;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao on 2020/8/11 17:07
 */
@Slf4j
@RestController
@Api(tags = {"访问控制 - 路由"})
public class RouteController extends BaseGatewayController {

    @Autowired
    private RouteService routeService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    @GetMapping("/routes/page")
    @ApiOperation(value = "路由:分页")
    public ResultMessage<Page<GatewayRoute>> getRoutesPage(RoutePageQuery query) {
        Page<GatewayRoute> result = routeService.getPageLike(
                query.getPage(), query.getLimit(), query.getRouteName(), query.getRoutePath());
        return ok(result);
    }

    @PostMapping("/routes")
    @ApiOperation(value = "路由:添加")
    public ResultMessage<Void> createRoute(@RequestBody CreateRouteCommand command) {
        routeService.createRoute(command);
        openRestTemplate.refreshGateway();
        return ok();
    }

    @PutMapping("/routes/{routeId}")
    @ApiOperation(value = "路由:修改")
    public ResultMessage<Void> modifyRoute(@PathVariable("routeId") Long routeId, @RequestBody ModifyRouteCommand command) {
        routeService.modifyRoute(routeId, command);
        openRestTemplate.refreshGateway();
        return ok();
    }

    @DeleteMapping("/routes/{routeId}")
    @ApiOperation(value = "路由:删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeId", value = "元素ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> removeRoute(@PathVariable("routeId") Long routeId) {
        routeService.removeRoute(routeId);
        openRestTemplate.refreshGateway();
        return ok();
    }
}