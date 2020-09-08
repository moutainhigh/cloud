package com.smart4y.cloud.hippo.access.application;

import com.smart4y.cloud.hippo.access.domain.entity.RbacMenu;
import com.smart4y.cloud.hippo.access.domain.entity.RbacPrivilege;
import com.smart4y.cloud.hippo.access.domain.entity.RbacRole;

import java.util.List;

/**
 * @author Youtao on 2020/8/10 14:38
 */
public interface UserApplicationService {

    /**
     * 获取用户所有角色
     * <p>
     * 用户角色
     * 用户所属组织关联角色
     * </p>
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RbacRole> getRoles(long userId);

    /**
     * 获取用户所有权限
     * <p>
     * 用户角色 对应权限
     * 用户所属组织关联角色 对应权限
     * </p>
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<RbacPrivilege> getPrivileges(long userId);

    /**
     * 获取用户所有菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<RbacMenu> getMenus(long userId);
}