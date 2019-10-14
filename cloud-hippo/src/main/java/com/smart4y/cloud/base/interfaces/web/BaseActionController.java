package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseActionService;
import com.smart4y.cloud.base.domain.model.BaseAction;
import com.smart4y.cloud.base.interfaces.converter.BaseActionConverter;
import com.smart4y.cloud.base.interfaces.valueobject.query.BaseActionQuery;
import com.smart4y.cloud.base.interfaces.valueobject.vo.BaseActionVO;
import com.smart4y.cloud.core.domain.page.Page;
import com.smart4y.cloud.core.domain.ResultEntity;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@RestController
@Api(tags = "系统功能按钮管理")
public class BaseActionController {

    @Autowired
    private BaseActionConverter baseActionConverter;
    @Autowired
    private BaseActionService baseActionService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页功能按钮列表
     */
    @GetMapping("/action")
    @ApiOperation(value = "获取分页功能按钮列表", notes = "获取分页功能按钮列表")
    public ResultEntity<Page<BaseActionVO>> findActionListPage(BaseActionQuery query) {
        PageInfo<BaseAction> listPage = baseActionService.findListPage(query);
        Page<BaseActionVO> page = baseActionConverter.convertPage(listPage);
        return ResultEntity.ok(page);
    }

    /**
     * 获取功能按钮详情
     */
    @ApiOperation(value = "获取功能按钮详情", notes = "获取功能按钮详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮Id", paramType = "path"),
    })
    @GetMapping("/action/{actionId}/info")
    public ResultEntity<BaseActionVO> getAction(
            @PathVariable("actionId") Long actionId) {
        BaseAction action = baseActionService.getAction(actionId);
        BaseActionVO vo = baseActionConverter.convert(action);
        return ResultEntity.ok(vo);
    }

    /**
     * 添加功能按钮
     *
     * @param actionCode 功能按钮编码
     * @param actionName 功能按钮名称
     * @param menuId     上级菜单
     * @param status     是否启用
     * @param priority   优先级越小越靠前
     * @param actionDesc 描述
     */
    @ApiOperation(value = "添加功能按钮", notes = "添加功能按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionCode", required = true, value = "功能按钮编码", paramType = "form"),
            @ApiImplicitParam(name = "actionName", required = true, value = "功能按钮名称", paramType = "form"),
            @ApiImplicitParam(name = "menuId", required = true, value = "上级菜单", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "actionDesc", required = false, value = "描述", paramType = "form"),
    })
    @PostMapping("/action/add")
    public ResultEntity<Long> addAction(
            @RequestParam(value = "actionCode") String actionCode,
            @RequestParam(value = "actionName") String actionName,
            @RequestParam(value = "menuId") Long menuId,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(value = "actionDesc", required = false, defaultValue = "") String actionDesc) {
        BaseAction action = new BaseAction();
        action.setActionCode(actionCode);
        action.setActionName(actionName);
        action.setMenuId(menuId);
        action.setStatus(status);
        action.setPriority(priority);
        action.setActionDesc(actionDesc);
        Long actionId = null;
        BaseAction result = baseActionService.addAction(action);
        if (result != null) {
            actionId = result.getActionId();
            openRestTemplate.refreshGateway();
        }
        return ResultEntity.ok(actionId);
    }

    /**
     * 编辑功能按钮
     *
     * @param actionId   功能按钮ID
     * @param actionCode 功能按钮编码
     * @param actionName 功能按钮名称
     * @param menuId     上级菜单
     * @param status     是否启用
     * @param priority   优先级越小越靠前
     * @param actionDesc 描述
     */
    @ApiOperation(value = "编辑功能按钮", notes = "添加功能按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form"),
            @ApiImplicitParam(name = "actionCode", required = true, value = "功能按钮编码", paramType = "form"),
            @ApiImplicitParam(name = "actionName", required = true, value = "功能按钮名称", paramType = "form"),
            @ApiImplicitParam(name = "menuId", required = true, value = "上级菜单", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "actionDesc", required = false, value = "描述", paramType = "form"),
    })
    @PostMapping("/action/update")
    public ResultEntity updateAction(
            @RequestParam("actionId") Long actionId,
            @RequestParam(value = "actionCode") String actionCode,
            @RequestParam(value = "actionName") String actionName,
            @RequestParam(value = "menuId") Long menuId,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(value = "actionDesc", required = false, defaultValue = "") String actionDesc) {
        BaseAction action = new BaseAction();
        action.setActionId(actionId);
        action.setActionCode(actionCode);
        action.setActionName(actionName);
        action.setMenuId(menuId);
        action.setStatus(status);
        action.setPriority(priority);
        action.setActionDesc(actionDesc);
        baseActionService.updateAction(action);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultEntity.ok();
    }

    /**
     * 移除功能按钮
     */
    @ApiOperation(value = "移除功能按钮", notes = "移除功能按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form")
    })
    @PostMapping("/action/remove")
    public ResultEntity removeAction(
            @RequestParam("actionId") Long actionId) {
        baseActionService.removeAction(actionId);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultEntity.ok();
    }
}