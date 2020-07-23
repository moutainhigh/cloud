package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseActionService;
import com.smart4y.cloud.base.domain.model.BaseAction;
import com.smart4y.cloud.base.interfaces.command.AddActionCommand;
import com.smart4y.cloud.base.interfaces.command.DeleteActionCommand;
import com.smart4y.cloud.base.interfaces.command.UpdateActionCommand;
import com.smart4y.cloud.base.interfaces.converter.BaseActionConverter;
import com.smart4y.cloud.base.interfaces.query.BaseActionQuery;
import com.smart4y.cloud.base.interfaces.vo.BaseActionVO;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.domain.page.Page;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
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
    public ResultMessage<Page<BaseActionVO>> findActionListPage(BaseActionQuery query) {
        PageInfo<BaseAction> listPage = baseActionService.findListPage(query);
        Page<BaseActionVO> page = baseActionConverter.convertPage(listPage);
        return ResultMessage.ok(page);
    }

    /**
     * 获取功能按钮详情
     */
    @ApiOperation(value = "获取功能按钮详情", notes = "获取功能按钮详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮Id", paramType = "path"),
    })
    @GetMapping("/action/{actionId}/info")
    public ResultMessage<BaseActionVO> getAction(@PathVariable("actionId") Long actionId) {
        BaseAction action = baseActionService.getAction(actionId);
        BaseActionVO vo = baseActionConverter.convert(action);
        return ResultMessage.ok(vo);
    }

    /**
     * 添加功能按钮
     */
    @PostMapping("/action/add")
    @ApiOperation(value = "添加功能按钮", notes = "添加功能按钮")
    public ResultMessage<Long> addAction(@RequestBody AddActionCommand command) {
        BaseAction action = new BaseAction();
        action.setActionCode(command.getActionCode());
        action.setActionName(command.getActionName());
        action.setMenuId(command.getMenuId());
        action.setStatus(command.getStatus());
        action.setPriority(command.getPriority());
        action.setActionDesc(command.getActionDesc());
        Long actionId = null;
        BaseAction result = baseActionService.addAction(action);
        if (result != null) {
            actionId = result.getActionId();
            openRestTemplate.refreshGateway();
        }
        return ResultMessage.ok(actionId);
    }

    /**
     * 编辑功能按钮
     */
    @PostMapping("/action/update")
    @ApiOperation(value = "编辑功能按钮", notes = "添加功能按钮")
    public ResultMessage<Void> updateAction(@RequestBody UpdateActionCommand command) {
        BaseAction action = new BaseAction();
        action.setActionId(command.getActionId());
        action.setActionCode(command.getActionCode());
        action.setActionName(command.getActionName());
        action.setMenuId(command.getMenuId());
        action.setStatus(command.getStatus());
        action.setPriority(command.getPriority());
        action.setActionDesc(command.getActionDesc());
        baseActionService.updateAction(action);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 移除功能按钮
     */
    @PostMapping("/action/remove")
    @ApiOperation(value = "移除功能按钮", notes = "移除功能按钮")
    public ResultMessage<Void> removeAction(@RequestBody DeleteActionCommand command) {
        baseActionService.removeAction(command.getActionId());
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}