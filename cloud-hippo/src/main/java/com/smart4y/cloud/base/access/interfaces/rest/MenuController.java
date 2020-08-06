package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.application.MenuApplicationService;
import com.smart4y.cloud.base.access.domain.model.RbacMenu;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.RbacMenuQuery;
import com.smart4y.cloud.core.message.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/7/30 15:03
 */
@Slf4j
@Api(tags = {"访问控制 - 菜单"})
@RestController
public class MenuController extends BaseAccessController {

    @Autowired
    private MenuApplicationService menuApplicationService;

    @GetMapping("/menus")
    @ApiOperation(value = "菜单:查询")
    public ResultMessage<List<RbacMenu>> getMenus() {
        List<RbacMenu> result = menuApplicationService.getMenus();
        return ok(result);
    }

    @GetMapping("/menus/children")
    @ApiOperation(value = "菜单:子级:查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单ID", defaultValue = "0", required = true, paramType = "query", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<List<RbacMenu>> getMenuChildren(RbacMenuQuery query) {
        List<RbacMenu> result = menuApplicationService.getMenuChildren(query);
        return ok(result);
    }

    @PostMapping("/menus")
    @ApiOperation(value = "菜单:添加")
    public ResultMessage<Void> createMenu(@RequestBody CreateMenuCommand command) {
        menuApplicationService.createMenu(command);
        return ok();
    }

    @GetMapping("/menus/{menuId}")
    @ApiOperation(value = "菜单:详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<RbacMenu> viewMenu(@PathVariable("menuId") Long menuId) {
        RbacMenu result = menuApplicationService.viewMenu(menuId);
        return ok(result);
    }
}