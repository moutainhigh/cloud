package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseRoleService;
import com.smart4y.cloud.base.domain.model.BaseRole;
import com.smart4y.cloud.base.domain.model.BaseRoleUser;
import com.smart4y.cloud.base.interfaces.converter.BaseRoleConverter;
import com.smart4y.cloud.base.interfaces.converter.BaseRoleUserConverter;
import com.smart4y.cloud.base.interfaces.valueobject.query.BaseRoleQuery;
import com.smart4y.cloud.base.interfaces.valueobject.vo.BaseRoleUserVO;
import com.smart4y.cloud.base.interfaces.valueobject.vo.BaseRoleVO;
import com.smart4y.cloud.core.domain.page.Page;
import com.smart4y.cloud.core.domain.ResultEntity;
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
 *         Created by youtao on 2019-09-05.
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
    public ResultEntity<Page<BaseRoleVO>> getRoleListPage(BaseRoleQuery query) {
        PageInfo<BaseRole> pageInfo = baseRoleService.findListPage(query);
        Page<BaseRoleVO> result = baseRoleConverter.convertPage(pageInfo);
        return ResultEntity.ok(result);
    }

    /**
     * 获取所有角色列表
     */
    @ApiOperation(value = "获取所有角色列表", notes = "获取所有角色列表")
    @GetMapping("/role/all")
    public ResultEntity<List<BaseRoleVO>> getRoleAllList() {
        List<BaseRole> list = baseRoleService.findAllList();
        List<BaseRoleVO> result = baseRoleConverter.convertList(list);
        return ResultEntity.ok(result);
    }

    /**
     * 查询角色成员
     */
    @ApiOperation(value = "查询角色成员", notes = "查询角色成员")
    @GetMapping("/role/users")
    public ResultEntity<List<BaseRoleUserVO>> getRoleUsers(
            @RequestParam(value = "roleId") Long roleId) {
        List<BaseRoleUser> roleUsers = baseRoleService.findRoleUsers(roleId);
        List<BaseRoleUserVO> result = baseRoleUserConverter.convertList(roleUsers);
        return ResultEntity.ok(result);
    }

    /**
     * 获取角色详情
     */
    @ApiOperation(value = "获取角色详情", notes = "获取角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "path")
    })
    @GetMapping("/role/{roleId}/info")
    public ResultEntity<BaseRoleVO> getRole(@PathVariable(value = "roleId") Long roleId) {
        BaseRole role = baseRoleService.getRole(roleId);
        BaseRoleVO result = baseRoleConverter.convert(role);
        return ResultEntity.ok(result);
    }

    /**
     * 添加角色
     *
     * @param roleCode 角色编码
     * @param roleName 角色显示名称
     * @param roleDesc 描述
     * @param status   启用禁用
     */
    @ApiOperation(value = "添加角色", notes = "添加角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleCode", value = "角色编码", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色显示名称", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form")
    })
    @PostMapping("/role/add")
    public ResultEntity<Long> addRole(
            @RequestParam(value = "roleCode") String roleCode,
            @RequestParam(value = "roleName") String roleName,
            @RequestParam(value = "roleDesc", required = false) String roleDesc,
            @RequestParam(value = "status", defaultValue = "1", required = false) Integer status
    ) {
        BaseRole role = new BaseRole();
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setStatus(status);
        role.setRoleDesc(roleDesc);
        Long roleId = null;
        BaseRole result = baseRoleService.addRole(role);
        if (result != null) {
            roleId = result.getRoleId();
        }
        return ResultEntity.ok(roleId);
    }

    /**
     * 编辑角色
     *
     * @param roleId   角色ID
     * @param roleCode 角色编码
     * @param roleName 角色显示名称
     * @param roleDesc 描述
     * @param status   启用禁用
     */
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleCode", value = "角色编码", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色显示名称", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form")
    })
    @PostMapping("/role/update")
    public ResultEntity updateRole(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "roleCode") String roleCode,
            @RequestParam(value = "roleName") String roleName,
            @RequestParam(value = "roleDesc", required = false) String roleDesc,
            @RequestParam(value = "status", defaultValue = "1", required = false) Integer status
    ) {
        BaseRole role = new BaseRole();
        role.setRoleId(roleId);
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setStatus(status);
        role.setRoleDesc(roleDesc);
        baseRoleService.updateRole(role);
        return ResultEntity.ok();
    }


    /**
     * 删除角色
     */
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form")
    })
    @PostMapping("/role/remove")
    public ResultEntity removeRole(
            @RequestParam(value = "roleId") Long roleId
    ) {
        baseRoleService.removeRole(roleId);
        return ResultEntity.ok();
    }

    /**
     * 角色添加成员
     */
    @ApiOperation(value = "角色添加成员", notes = "角色添加成员")
    @PostMapping("/role/users/add")
    public ResultEntity addUserRoles(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "userIds", required = false) String userIds) {
        List<Long> collect = Arrays.stream(userIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseRoleService.saveRoleUsers(roleId, collect);
        return ResultEntity.ok();
    }
}