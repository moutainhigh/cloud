package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.RbacUserApplicationService;
import com.smart4y.cloud.base.access.domain.entity.*;
import com.smart4y.cloud.base.access.domain.service.*;
import com.smart4y.cloud.core.annotation.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao on 2020/8/10 14:46
 */
@Slf4j
@ApplicationService
public class RbacUserApplicationServiceImpl implements RbacUserApplicationService {

    private final GroupUserService groupUserService;
    private final GroupRoleService groupRoleService;
    private final RoleService roleService;
    private final RolePrivilegeService rolePrivilegeService;
    private final PrivilegeService privilegeService;
    private final PrivilegeMenuService privilegeMenuService;
    private final MenuService menuService;
    private final UserService userService;

    @Autowired
    public RbacUserApplicationServiceImpl(GroupUserService groupUserService, GroupRoleService groupRoleService, RoleService roleService, RolePrivilegeService rolePrivilegeService, PrivilegeService privilegeService, PrivilegeMenuService privilegeMenuService, MenuService menuService, UserService userService) {
        this.groupUserService = groupUserService;
        this.groupRoleService = groupRoleService;
        this.roleService = roleService;
        this.rolePrivilegeService = rolePrivilegeService;
        this.privilegeService = privilegeService;
        this.privilegeMenuService = privilegeMenuService;
        this.menuService = menuService;
        this.userService = userService;
    }

    @Override
    public List<RbacRole> getRbacGroupRoles(long userId) {
        // 获取用户所属组织关联角色
        List<RbacGroupUser> groupUsers = groupUserService.getGroups(userId);
        List<Long> groupIds = groupUsers.stream().map(RbacGroupUser::getGroupId).collect(Collectors.toList());
        List<RbacGroupRole> groupRoles = groupRoleService.getRoles(groupIds);
        List<Long> roleIds = groupRoles.stream().map(RbacGroupRole::getRoleId).collect(Collectors.toList());

        return roleService.getByIds(roleIds);
    }

    @Override
    public List<RbacRole> getAllRoles(long userId) {
        // 获取用户角色
        List<RbacUserRole> userRoles = userService.getUserRolesByUserId(userId);
        List<Long> roleIds = userRoles.stream().map(RbacUserRole::getRoleId).collect(Collectors.toList());

        // 获取用户所属组织关联角色
        List<Long> groupRoleIds = this.getRbacGroupRoles(userId).stream()
                .map(RbacRole::getRoleId).collect(Collectors.toList());

        roleIds.addAll(groupRoleIds);

        return roleService.getByIds(roleIds);
    }

    @Override
    public List<RbacPrivilege> getAllPrivileges(long userId) {
        // 用户所有角色
        List<RbacRole> allRoles = this.getAllRoles(userId);
        List<Long> roleIds = allRoles.stream().map(RbacRole::getRoleId).collect(Collectors.toList());

        // 获取角色关联的权限
        List<RbacRolePrivilege> rolePrivileges = rolePrivilegeService.getPrivileges(roleIds);
        List<Long> privilegeIds = rolePrivileges.stream().map(RbacRolePrivilege::getPrivilegeId).collect(Collectors.toList());

        return privilegeService.getPrivileges(privilegeIds);
    }

    @Override
    public List<RbacMenu> getAllMenus(long userId) {
        // 用户所有权限
        List<RbacPrivilege> allPrivileges = this.getAllPrivileges(userId);
        List<Long> privilegeIds = allPrivileges.stream().map(RbacPrivilege::getPrivilegeId).collect(Collectors.toList());

        // 获取权限菜单
        List<RbacPrivilegeMenu> privilegeMenus = privilegeMenuService.getMenus(privilegeIds);
        List<Long> menuIds = privilegeMenus.stream().map(RbacPrivilegeMenu::getMenuId).collect(Collectors.toList());

        return menuService.getByIds(menuIds).stream()
                .filter(x -> "10".equals(x.getMenuState()))
                .collect(Collectors.toList());
    }
}