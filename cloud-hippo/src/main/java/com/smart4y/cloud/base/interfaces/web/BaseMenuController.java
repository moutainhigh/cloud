package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseActionService;
import com.smart4y.cloud.base.application.BaseMenuService;
import com.smart4y.cloud.base.domain.model.BaseAction;
import com.smart4y.cloud.base.domain.model.BaseMenu;
import com.smart4y.cloud.base.interfaces.converter.BaseActionConverter;
import com.smart4y.cloud.base.interfaces.converter.BaseMenuConverter;
import com.smart4y.cloud.base.interfaces.valueobject.command.AddMenuCommand;
import com.smart4y.cloud.base.interfaces.valueobject.query.BaseMenuQuery;
import com.smart4y.cloud.base.interfaces.valueobject.vo.BaseActionVO;
import com.smart4y.cloud.base.interfaces.valueobject.vo.BaseMenuVO;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.domain.page.Page;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
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
     *
     * @param menuCode 菜单编码
     * @param menuName 菜单名称
     * @param icon     图标
     * @param scheme   请求前缀
     * @param path     请求路径
     * @param target   打开方式
     * @param status   是否启用
     * @param parentId 父节点ID
     * @param priority 优先级越小越靠前
     * @param menuDesc 描述
     */
    @ApiOperation(value = "编辑菜单资源", notes = "编辑菜单资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", required = true, value = "菜单ID", paramType = "form"),
            @ApiImplicitParam(name = "menuCode", required = true, value = "菜单编码", paramType = "form"),
            @ApiImplicitParam(name = "menuName", required = true, value = "菜单名称", paramType = "form"),
            @ApiImplicitParam(name = "icon", required = false, value = "图标", paramType = "form"),
            @ApiImplicitParam(name = "scheme", required = false, value = "请求协议", allowableValues = "/,http://,https://", paramType = "form"),
            @ApiImplicitParam(name = "path", required = false, value = "请求路径", paramType = "form"),
            @ApiImplicitParam(name = "target", required = false, value = "请求路径", allowableValues = "_self,_blank", paramType = "form"),
            @ApiImplicitParam(name = "parentId", required = false, defaultValue = "0", value = "父节点ID", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "menuDesc", required = false, value = "描述", paramType = "form"),
    })
    @PostMapping("/menu/update")
    public ResultMessage updateMenu(
            @RequestParam("menuId") Long menuId,
            @RequestParam(value = "menuCode") String menuCode,
            @RequestParam(value = "menuName") String menuName,
            @RequestParam(value = "icon", required = false) String icon,
            @RequestParam(value = "scheme", required = false, defaultValue = "/") String scheme,
            @RequestParam(value = "path", required = false, defaultValue = "") String path,
            @RequestParam(value = "target", required = false, defaultValue = "_self") String target,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "parentId", required = false, defaultValue = "0") Long parentId,
            @RequestParam(value = "priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(value = "menuDesc", required = false, defaultValue = "") String menuDesc
    ) {
        BaseMenu menu = new BaseMenu();
        menu.setMenuId(menuId);
        menu.setMenuCode(menuCode);
        menu.setMenuName(menuName);
        menu.setIcon(icon);
        menu.setPath(path);
        menu.setScheme(scheme);
        menu.setTarget(target);
        menu.setStatus(status);
        menu.setParentId(parentId);
        menu.setPriority(priority);
        menu.setMenuDesc(menuDesc);
        baseMenuService.updateMenu(menu);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 移除菜单资源
     */
    @ApiOperation(value = "移除菜单资源", notes = "移除菜单资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", required = true, value = "menuId", paramType = "form"),
    })
    @PostMapping("/menu/remove")
    public ResultMessage<Boolean> removeMenu(
            @RequestParam("menuId") Long menuId
    ) {
        baseMenuService.removeMenu(menuId);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}