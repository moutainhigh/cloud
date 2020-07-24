package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseRoleService;
import com.smart4y.cloud.base.domain.model.BaseRole;
import com.smart4y.cloud.base.domain.model.BaseRoleUser;
import com.smart4y.cloud.base.interfaces.command.role.AddRoleCommand;
import com.smart4y.cloud.base.interfaces.command.role.AddRoleUserCommand;
import com.smart4y.cloud.base.interfaces.command.role.DeleteRoleCommand;
import com.smart4y.cloud.base.interfaces.command.role.UpdateRoleCommand;
import com.smart4y.cloud.base.interfaces.converter.BaseRoleConverter;
import com.smart4y.cloud.base.interfaces.converter.BaseRoleUserConverter;
import com.smart4y.cloud.base.interfaces.query.BaseRoleQuery;
import com.smart4y.cloud.base.interfaces.vo.BaseRoleUserVO;
import com.smart4y.cloud.base.interfaces.vo.BaseRoleVO;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.domain.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Api(tags = "系统角色管理")
@RestController
public class BaseRoleController {

    @Autowired
    private BaseRoleUserConverter baseRoleUserConverter;
    @Autowired
    private BaseRoleConverter baseRoleConverter;
    @Autowired
    private BaseRoleService baseRoleService;

    /**
     * 获取分页角色列表
     */
    @ApiOperation(value = "获取分页角色列表", notes = "获取分页角色列表")
    @GetMapping("/role")
    public ResultMessage<Page<BaseRoleVO>> getRoleListPage(BaseRoleQuery query) {
        PageInfo<BaseRole> pageInfo = baseRoleService.findListPage(query);
        Page<BaseRoleVO> result = baseRoleConverter.convertPage(pageInfo);
        return ResultMessage.ok(result);
    }

    /**
     * 获取所有角色列表
     */
    @ApiOperation(value = "获取所有角色列表", notes = "获取所有角色列表")
    @GetMapping("/role/all")
    public ResultMessage<List<BaseRoleVO>> getRoleAllList() {
        List<BaseRole> list = baseRoleService.findAllList();
        List<BaseRoleVO> result = baseRoleConverter.convertList(list);
        return ResultMessage.ok(result);
    }

    /**
     * 查询角色成员
     */
    @ApiOperation(value = "查询角色成员", notes = "查询角色成员")
    @GetMapping("/role/users")
    public ResultMessage<List<BaseRoleUserVO>> getRoleUsers(@RequestParam(value = "roleId") Long roleId) {
        List<BaseRoleUser> roleUsers = baseRoleService.findRoleUsers(roleId);
        List<BaseRoleUserVO> result = baseRoleUserConverter.convertList(roleUsers);
        return ResultMessage.ok(result);
    }

    /**
     * 获取角色详情
     */
    @ApiOperation(value = "获取角色详情", notes = "获取角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "path")
    })
    @GetMapping("/role/{roleId}/info")
    public ResultMessage<BaseRoleVO> getRole(@PathVariable(value = "roleId") Long roleId) {
        BaseRole role = baseRoleService.getRole(roleId);
        BaseRoleVO result = baseRoleConverter.convert(role);
        return ResultMessage.ok(result);
    }

    /**
     * 添加角色
     */
    @ApiOperation(value = "添加角色", notes = "添加角色")
    @PostMapping("/role/add")
    public ResultMessage<Long> addRole(@RequestBody AddRoleCommand command) {
        BaseRole role = new BaseRole();
        role.setRoleCode(command.getRoleCode());
        role.setRoleName(command.getRoleName());
        role.setStatus(command.getStatus());
        role.setRoleDesc(command.getRoleDesc());
        Long roleId = null;
        BaseRole result = baseRoleService.addRole(role);
        if (result != null) {
            roleId = result.getRoleId();
        }
        return ResultMessage.ok(roleId);
    }

    /**
     * 编辑角色
     */
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    @PostMapping("/role/update")
    public ResultMessage<Void> updateRole(@RequestBody UpdateRoleCommand command) {
        BaseRole role = new BaseRole();
        role.setRoleId(command.getRoleId());
        role.setRoleCode(command.getRoleCode());
        role.setRoleName(command.getRoleName());
        role.setStatus(command.getStatus());
        role.setRoleDesc(command.getRoleDesc());
        baseRoleService.updateRole(role);
        return ResultMessage.ok();
    }

    /**
     * 删除角色
     */
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @PostMapping("/role/remove")
    public ResultMessage<Void> removeRole(@RequestBody DeleteRoleCommand command) {
        baseRoleService.removeRole(command.getRoleId());
        return ResultMessage.ok();
    }

    /**
     * 角色添加成员
     */
    @ApiOperation(value = "角色添加成员", notes = "角色添加成员")
    @PostMapping("/role/users/add")
    public ResultMessage<Void> addUserRoles(@RequestBody AddRoleUserCommand command) {
        List<Long> collect = Arrays.stream(command.getUserIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseRoleService.saveRoleUsers(command.getRoleId(), collect);
        return ResultMessage.ok();
    }
}