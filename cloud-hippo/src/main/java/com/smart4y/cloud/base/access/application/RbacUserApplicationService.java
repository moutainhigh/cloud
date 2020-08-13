package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.entity.RbacMenu;
import com.smart4y.cloud.base.access.domain.entity.RbacPrivilege;
import com.smart4y.cloud.base.access.domain.entity.RbacRole;

import java.util.List;

/**
 * @author Youtao on 2020/8/10 14:38
 */
public interface RbacUserApplicationService {

    /**
     * 获取用户直接授予的角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RbacRole> getRbacRoles(long userId);

    /**
     * 获取用户所属组织授予的角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RbacRole> getRbacGroupRoles(long userId);

    /**
     * 获取用户所有角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RbacRole> getAllRoles(long userId);

    /**
     * 获取用户所有权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<RbacPrivilege> getAllPrivileges(long userId);

    /**
     * 获取用户所有菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<RbacMenu> getAllMenus(long userId);
}