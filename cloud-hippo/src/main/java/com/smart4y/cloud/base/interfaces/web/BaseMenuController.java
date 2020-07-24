package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseActionService;
import com.smart4y.cloud.base.application.BaseMenuService;
import com.smart4y.cloud.base.domain.model.BaseAction;
import com.smart4y.cloud.base.domain.model.BaseMenu;
import com.smart4y.cloud.base.interfaces.command.menu.AddMenuCommand;
import com.smart4y.cloud.base.interfaces.command.menu.DeleteMenuCommand;
import com.smart4y.cloud.base.interfaces.command.menu.UpdateMenuCommand;
import com.smart4y.cloud.base.interfaces.converter.BaseActionConverter;
import com.smart4y.cloud.base.interfaces.converter.BaseMenuConverter;
import com.smart4y.cloud.base.interfaces.query.BaseMenuQuery;
import com.smart4y.cloud.base.interfaces.vo.BaseActionVO;
import com.smart4y.cloud.base.interfaces.vo.BaseMenuVO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@RestController
@Api(tags = "系统菜单资源管理")
public class BaseMenuController {

    @Autowired
    private BaseMenuConverter baseMenuConverter;
    @Autowired
    private BaseActionConverter baseActionConverter;
    @Autowired
    private BaseMenuService baseMenuService;
    @Autowired
    private BaseActionService baseActionService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页菜单资源列表
     */
    @GetMapping("/menu")
    @ApiOperation(value = "获取分页菜单资源列表", notes = "获取分页菜单资源列表")
    public ResultMessage<Page<BaseMenuVO>> getMenuListPage(BaseMenuQuery query) {
        PageInfo<BaseMenu> pageInfo = baseMenuService.findListPage(query);
        Page<BaseMenuVO> result = baseMenuConverter.convertPage(pageInfo);
        return ResultMessage.ok(result);
    }

    /**
     * 菜单所有资源列表
     */
    @ApiOperation(value = "菜单所有资源列表", notes = "菜单所有资源列表")
    @GetMapping("/menu/all")
    public ResultMessage<List<BaseMenuVO>> getMenuAllList() {
        List<BaseMenu> list = baseMenuService.findAllList();
        List<BaseMenuVO> result = baseMenuConverter.convertList(list);
        return ResultMessage.ok(result);
    }


    /**
     * 获取菜单下所有操作
     */
    @ApiOperation(value = "获取菜单下所有操作", notes = "获取菜单下所有操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "menuId", paramType = "form"),
    })
    @GetMapping("/menu/action")
    public ResultMessage<List<BaseActionVO>> getMenuAction(Long menuId) {
        List<BaseAction> list = baseActionService.findListByMenuId(menuId);
        List<BaseActionVO> result = baseActionConverter.convertList(list);
        return ResultMessage.ok(result);
    }

    /**
     * 获取菜单资源详情
     *
     * @return 应用信息
     */
    @ApiOperation(value = "获取菜单资源详情", notes = "获取菜单资源详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", required = true, value = "menuId"),
    })
    @GetMapping("/menu/{menuId}/info")
    public ResultMessage<BaseMenuVO> getMenu(@PathVariable("menuId") Long menuId) {
        BaseMenu menu = baseMenuService.getMenu(menuId);
        BaseMenuVO result = baseMenuConverter.convert(menu);
        return ResultMessage.ok(result);
    }

    /**
     * 添加菜单资源
     */
    @PostMapping(value = "/menu/add")
    @ApiOperation(value = "添加菜单资源", notes = "添加菜单资源")
    public ResultMessage<Long> addMenu(@RequestBody AddMenuCommand command) {
        BaseMenu menu = new BaseMenu();
        menu.setMenuCode(command.getMenuCode());
        menu.setMenuName(command.getMenuName());
        menu.setIcon(command.getIcon());
        menu.setPath(command.getPath());
        menu.setScheme(command.getScheme());
        menu.setTarget(command.getTarget());
        menu.setStatus(command.getStatus());
        menu.setParentId(command.getParentId());
        menu.setPriority(command.getPriority());
        menu.setMenuDesc(command.getMenuDesc());
        Long menuId = null;
        BaseMenu result = baseMenuService.addMenu(menu);
        if (result != null) {
            menuId = result.getMenuId();
        }
        return ResultMessage.ok(menuId);
    }

    /**
     * 编辑菜单资源
     */
    @ApiOperation(value = "编辑菜单资源", notes = "编辑菜单资源")
    @PostMapping("/menu/update")
    public ResultMessage<Void> updateMenu(@RequestBody UpdateMenuCommand command) {
        BaseMenu menu = new BaseMenu();
        menu.setMenuId(command.getMenuId());
        menu.setMenuCode(command.getMenuCode());
        menu.setMenuName(command.getMenuName());
        menu.setIcon(command.getIcon());
        menu.setPath(command.getPath());
        menu.setScheme(command.getScheme());
        menu.setTarget(command.getTarget());
        menu.setStatus(command.getStatus());
        menu.setParentId(command.getParentId());
        menu.setPriority(command.getPriority());
        menu.setMenuDesc(command.getMenuDesc());
        baseMenuService.updateMenu(menu);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 移除菜单资源
     */
    @ApiOperation(value = "移除菜单资源", notes = "移除菜单资源")
    @PostMapping("/menu/remove")
    public ResultMessage<Boolean> removeMenu(@RequestBody DeleteMenuCommand command) {
        baseMenuService.removeMenu(command.getMenuId());
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}