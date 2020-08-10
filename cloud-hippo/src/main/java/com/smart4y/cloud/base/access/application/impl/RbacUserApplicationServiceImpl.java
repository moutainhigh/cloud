package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.RbacUserApplicationService;
import com.smart4y.cloud.base.access.application.UserApplicationService;
import com.smart4y.cloud.base.access.domain.model.*;
import com.smart4y.cloud.base.access.domain.service.*;
import com.smart4y.cloud.base.access.interfaces.dtos.user.RbacUserPageQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.message.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao on 2020/8/10 14:46
 */
@Slf4j
@ApplicationService
public class RbacUserApplicationServiceImpl implements RbacUserApplicationService, UserApplicationService {

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private GroupUserService groupUserService;
    @Autowired
    private GroupRoleService groupRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RolePrivilegeService rolePrivilegeService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private PrivilegeMenuService privilegeMenuService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;

    @Override
    public Page<RbacUser> getUsersPage(RbacUserPageQuery query) {
        Weekend<RbacUser> weekend = Weekend.of(RbacUser.class);
        WeekendCriteria<RbacUser, Object> criteria = weekend.weekendCriteria();
        if (null != query.getUserId()) {
            criteria.andLike(RbacUser::getUserId, "%" + query.getUserId() + "%");
        }
        if (StringUtils.isNotBlank(query.getUserName())) {
            criteria.andLike(RbacUser::getUserName, "%" + query.getUserName() + "%");
        }
        weekend
                .orderBy("createdDate").desc();

        return userService.findPage(weekend, query.getPage(), query.getLimit());
    }

    @Override
    public List<RbacRole> getRbacRoles(long userId) {
        // 获取用户角色
        List<RbacUserRole> userRoles = userRoleService.getRoles(userId);
        List<Long> roleIds = userRoles.stream().map(RbacUserRole::getRoleId).collect(Collectors.toList());

        return roleService.getRoles(roleIds);
    }

    @Override
    public List<RbacRole> getRbacGroupRoles(long userId) {
        // 获取用户所属组织关联角色
        List<RbacGroupUser> groupUsers = groupUserService.getGroups(userId);
        List<Long> groupIds = groupUsers.stream().map(RbacGroupUser::getGroupId).collect(Collectors.toList());
        List<RbacGroupRole> groupRoles = groupRoleService.getRoles(groupIds);
        List<Long> roleIds = groupRoles.stream().map(RbacGroupRole::getRoleId).collect(Collectors.toList());

        return roleService.getRoles(roleIds);
    }

    @Override
    public List<RbacRole> getAllRoles(long userId) {
        // 获取用户角色
        List<Long> roleIds = this.getRbacRoles(userId).stream()
                .map(RbacRole::getRoleId).collect(Collectors.toList());
        // 获取用户所属组织关联角色
        List<Long> groupRoleIds = this.getRbacGroupRoles(userId).stream()
                .map(RbacRole::getRoleId).collect(Collectors.toList());

        roleIds.addAll(groupRoleIds);

        return roleService.getRoles(roleIds);
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

        return menuService.getMenus(menuIds);
    }
}