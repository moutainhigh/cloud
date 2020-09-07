package com.smart4y.cloud.access.interfaces.rest;

import com.smart4y.cloud.access.application.PrivilegeApplicationService;
import com.smart4y.cloud.access.domain.entity.RbacMenu;
import com.smart4y.cloud.access.domain.service.MenuService;
import com.smart4y.cloud.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.access.interfaces.dtos.menu.ModifyMenuCommand;
import com.smart4y.cloud.access.interfaces.dtos.menu.RbacMenuQuery;
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

    private final MenuService menuService;
    private final PrivilegeApplicationService privilegeApplicationService;

    @Autowired
    public MenuController(MenuService menuService, PrivilegeApplicationService privilegeApplicationService) {
        this.menuService = menuService;
        this.privilegeApplicationService = privilegeApplicationService;
    }

    @GetMapping("/menus")
    @ApiOperation(value = "菜单:查询")
    public ResultMessage<List<RbacMenu>> getMenus() {
        List<RbacMenu> result = menuService.list();
        return ok(result);
    }

    @GetMapping("/menus/children")
    @ApiOperation(value = "菜单:子级:查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单ID", defaultValue = "0", required = true, paramType = "query", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<List<RbacMenu>> getMenuChildren(RbacMenuQuery query) {
        List<RbacMenu> result = menuService.getChildrenByParentId(query.getMenuId());
        return ok(result);
    }

    @PostMapping("/menus")
    @ApiOperation(value = "菜单:添加")
    public ResultMessage<Void> createMenu(@RequestBody CreateMenuCommand command) {
        privilegeApplicationService.createMenu(command);
        return ok();
    }

    @PutMapping("/menus/{menuId}")
    @ApiOperation(value = "菜单:修改")
    public ResultMessage<Void> modifyMenu(@PathVariable("menuId") Long menuId, @RequestBody ModifyMenuCommand command) {
        privilegeApplicationService.modifyMenu(menuId, command);
        return ok();
    }

    @DeleteMapping("/menus/{menuId}")
    @ApiOperation(value = "菜单:移除")
    public ResultMessage<Void> removeMenu(@PathVariable("menuId") Long menuId) {
        privilegeApplicationService.removeMenu(menuId);
        return ok();
    }

    @GetMapping("/menus/{menuId}")
    @ApiOperation(value = "菜单:详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<RbacMenu> viewMenu(@PathVariable("menuId") Long menuId) {
        RbacMenu result = menuService.getById(menuId);
        return ok(result);
    }
}