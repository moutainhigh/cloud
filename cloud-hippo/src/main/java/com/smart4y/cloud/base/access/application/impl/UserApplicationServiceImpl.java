package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.UserApplicationService;
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
public class UserApplicationServiceImpl implements UserApplicationService {

    private final GroupService groupService;
    private final RoleService roleService;
    private final PrivilegeService privilegeService;
    private final PrivilegeMenuService privilegeMenuService;
    private final MenuService menuService;
    private final UserService userService;

    @Autowired
    public UserApplicationServiceImpl(RoleService roleService, PrivilegeService privilegeService, PrivilegeMenuService privilegeMenuService, MenuService menuService, UserService userService, GroupService groupService) {
        this.roleService = roleService;
        this.privilegeService = privilegeService;
        this.privilegeMenuService = privilegeMenuService;
        this.menuService = menuService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @Override
    public List<RbacRole> getRoles(long userId) {
        List<Long> roleIds = getRoleIds(userId);
        return roleService.getByIds(roleIds);
    }

    @Override
    public List<RbacPrivilege> getPrivileges(long userId) {
        List<Long> privilegeIds = getPrivilegeIds(userId);
        return privilegeService.getByIds(privilegeIds);
    }

    @Override
    public List<RbacMenu> getMenus(long userId) {
        List<Long> privilegeIds = this.getPrivilegeIds(userId);

        // 获取权限菜单
        List<RbacPrivilegeMenu> privilegeMenus = privilegeMenuService.getMenus(privilegeIds);
        List<Long> menuIds = privilegeMenus.stream()
                .map(RbacPrivilegeMenu::getMenuId)
                .collect(Collectors.toList());

        return menuService.getByIds(menuIds).stream()
                .filter(x -> "10".equals(x.getMenuState()))
                .collect(Collectors.toList());
    }

    /**
     * 获取用户角色
     * <p>
     * 用户角色
     * 用户所属组织关联角色
     * </p>
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    private List<Long> getRoleIds(long userId) {
        // 获取用户角色
        List<RbacUserRole> userRoles = userService.getUserRolesByUserId(userId);
        List<Long> roleIds = userRoles.stream().map(RbacUserRole::getRoleId).collect(Collectors.toList());
        // 获取用户所属组织关联角色
        List<RbacGroupUser> groupUsers = groupService.getUserGroupsByUserId(userId);
        List<Long> groupIds = groupUsers.stream().map(RbacGroupUser::getGroupId).collect(Collectors.toList());
        List<RbacGroupRole> groupRoles = groupService.getGroupRolesByGroupIds(groupIds);
        List<Long> groupRoleIds = groupRoles.stream().map(RbacGroupRole::getRoleId).collect(Collectors.toList());
        roleIds.addAll(groupRoleIds);
        return roleIds;
    }

    /**
     * 获取用户权限
     * <p>
     * 用户角色 对应权限
     * 用户所属组织关联角色 对应权限
     * </p>
     *
     * @param userId 用户ID
     * @return 权限ID列表
     */
    private List<Long> getPrivilegeIds(long userId) {
        // 获取用户角色
        List<Long> roleIds = this.getRoleIds(userId);
        // 获取角色关联的权限
        List<RbacRolePrivilege> rolePrivileges = roleService.getRolePrivilegesByRoleIds(roleIds);
        return rolePrivileges.stream()
                .map(RbacRolePrivilege::getPrivilegeId)
                .collect(Collectors.toList());
    }
}