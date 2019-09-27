package com.smart4y.cloud.base.application;

import com.smart4y.cloud.base.domain.model.BaseRole;
import com.smart4y.cloud.base.domain.model.BaseRoleUser;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.PageParams;

import java.util.List;

/**
 * 角色管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface BaseRoleService {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<BaseRole> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<BaseRole> findAllList();

    /**
     * 获取角色信息
     *
     * @param roleId
     * @return
     */
    BaseRole getRole(long roleId);

    /**
     * 添加角色
     *
     * @param role 角色
     * @return
     */
    BaseRole addRole(BaseRole role);

    /**
     * 更新角色
     *
     * @param role 角色
     * @return
     */
    BaseRole updateRole(BaseRole role);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return
     */
    void removeRole(long roleId);

    /**
     * 检测角色编码是否存在
     *
     * @param roleCode
     * @return
     */
    Boolean isExist(String roleCode);

    /**
     * 用户添加角色
     *
     * @param userId
     * @param roles
     * @return
     */
    void saveUserRoles(long userId, List<Long> roles);

    /**
     * 角色添加成员
     *
     * @param roleId
     * @param userIds
     */
    void saveRoleUsers(long roleId, List<Long> userIds);

    /**
     * 查询角色成员
     *
     * @return
     */
    List<BaseRoleUser> findRoleUsers(long roleId);

    /**
     * 获取角色所有授权组员数量
     *
     * @param roleId
     * @return
     */
    int getCountByRole(long roleId);

    /**
     * 获取组员角色数量
     *
     * @param userId
     * @return
     */
    int getCountByUser(long userId);

    /**
     * 移除角色所有组员
     *
     * @param roleId
     * @return
     */
    void removeRoleUsers(long roleId);

    /**
     * 移除组员的所有角色
     *
     * @param userId
     * @return
     */
    void removeUserRoles(long userId);

    /**
     * 检测是否存在
     *
     * @param userId
     * @param roleId
     * @return
     */
    Boolean isExist(long userId, long roleId);

    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     */
    List<BaseRole> getUserRoles(long userId);

    /**
     * 获取用户角色ID列表
     *
     * @param userId
     * @return
     */
    List<Long> getUserRoleIds(long userId);
}
