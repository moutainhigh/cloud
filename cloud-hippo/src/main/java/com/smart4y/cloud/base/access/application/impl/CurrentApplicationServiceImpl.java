package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.CurrentApplicationService;
import com.smart4y.cloud.base.access.domain.model.*;
import com.smart4y.cloud.base.access.domain.service.*;
import com.smart4y.cloud.core.annotation.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao
 * on 2020/8/7 14:24
 */
@Slf4j
@ApplicationService
public class CurrentApplicationServiceImpl implements CurrentApplicationService {

    private final UserRoleService userRoleService;
    private final GroupUserService groupUserService;
    private final GroupRoleService groupRoleService;
    private final RolePrivilegeService rolePrivilegeService;
    @Autowired
    private PrivilegeMenuService privilegeMenuService;
    @Autowired
    private MenuService menuService;

    @Autowired
    public CurrentApplicationServiceImpl(UserRoleService userRoleService, GroupUserService groupUserService, GroupRoleService groupRoleService, RolePrivilegeService rolePrivilegeService) {
        this.userRoleService = userRoleService;
        this.groupUserService = groupUserService;
        this.groupRoleService = groupRoleService;
        this.rolePrivilegeService = rolePrivilegeService;
    }

    @Override
    public List<RbacMenu> getCurrentUserMenus(long userId) {
        // 获取用户角色
        List<RbacUserRole> userRoles = userRoleService.getRoles(userId);
        // 获取用户所属组织关联角色
        List<RbacGroupUser> groupUsers = groupUserService.getGroups(userId);
        List<Long> groupIds = groupUsers.stream().map(RbacGroupUser::getGroupId).collect(Collectors.toList());
        List<RbacGroupRole> groupRoles = groupRoleService.getRoles(groupIds);
        // 用户所有角色
        List<Long> roleIds = userRoles.stream().map(RbacUserRole::getRoleId).collect(Collectors.toList());
        roleIds.addAll(groupRoles.stream().map(RbacGroupRole::getRoleId).collect(Collectors.toList()));
        // 获取角色关联的权限
        List<RbacRolePrivilege> rolePrivileges = rolePrivilegeService.getPrivileges(roleIds);
        List<Long> privilegeIds = rolePrivileges.stream().map(RbacRolePrivilege::getPrivilegeId).collect(Collectors.toList());
        // 获取权限菜单
        List<RbacPrivilegeMenu> privilegeMenus = privilegeMenuService.getMenus(privilegeIds);
        List<Long> menuIds = privilegeMenus.stream().map(RbacPrivilegeMenu::getMenuId).collect(Collectors.toList());
        // 获取菜单
        return menuService.getMenus(menuIds);
    }
}