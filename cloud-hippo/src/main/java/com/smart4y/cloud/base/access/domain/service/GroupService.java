package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacGroup;
import com.smart4y.cloud.base.access.domain.entity.RbacGroupRole;
import com.smart4y.cloud.base.access.domain.entity.RbacGroupUser;
import com.smart4y.cloud.base.access.infrastructure.persistence.mybatis.RbacGroupRoleMapper;
import com.smart4y.cloud.base.access.infrastructure.persistence.mybatis.RbacGroupUserMapper;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.List;

/**
 * @author Youtao on 2020/8/13 17:23
 */
@DomainService
public class GroupService extends BaseDomainService<RbacGroup> {

    @Autowired
    private RbacGroupUserMapper rbacGroupUserMapper;
    @Autowired
    private RbacGroupRoleMapper rbacGroupRoleMapper;

    /**
     * 获取指定组织的子节点
     *
     * @param parentId 父级ID
     * @return 子节点组织列表
     */
    public List<RbacGroup> getChildrenByParentId(long parentId) {
        Weekend<RbacGroup> weekend = Weekend.of(RbacGroup.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroup::getGroupParentId, parentId);
        weekend
                .orderBy("createdDate").asc();
        return this.list(weekend);
    }

    /**
     * 获取指定组织的用户列表
     *
     * @param groupId 组织ID
     * @return 用户列表
     */
    public List<RbacGroupUser> getGroupUsersByGroupId(long groupId) {
        Weekend<RbacGroupUser> weekend = Weekend.of(RbacGroupUser.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroupUser::getGroupId, groupId);
        return rbacGroupUserMapper.selectByExample(weekend);
    }

    /**
     * 获取指定组织的角色列表
     *
     * @param groupId 组织ID
     * @return 角色列表
     */
    public List<RbacGroupRole> getGroupRolesByGroupId(long groupId) {
        Weekend<RbacGroupRole> weekend = Weekend.of(RbacGroupRole.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroupRole::getGroupId, groupId);
        return rbacGroupRoleMapper.selectByExample(weekend);
    }

    /**
     * 获取指定组织的角色列表
     *
     * @param groupIds 组织ID列表
     * @return 角色列表
     */
    public List<RbacGroupRole> getGroupRolesByGroupIds(Collection<Long> groupIds) {
        Weekend<RbacGroupRole> weekend = Weekend.of(RbacGroupRole.class);
        weekend
                .weekendCriteria()
                .andIn(RbacGroupRole::getGroupId, groupIds);
        return rbacGroupRoleMapper.selectByExample(weekend);
    }

    /**
     * 获取指定用户所属的组织列表
     *
     * @param userId 用户ID
     * @return 组织列表
     */
    public List<RbacGroupUser> getUserGroupsByUserId(long userId) {
        Weekend<RbacGroupUser> weekend = Weekend.of(RbacGroupUser.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroupUser::getUserId, userId);
        return rbacGroupUserMapper.selectByExample(weekend);
    }

    /**
     * 获取指定角色所属的组织列表
     *
     * @param roleId 角色ID
     * @return 组织列表
     */
    public List<RbacGroupRole> getRoleGroupsByRoleId(long roleId) {
        Weekend<RbacGroupRole> weekend = Weekend.of(RbacGroupRole.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroupRole::getRoleId, roleId);
        return rbacGroupRoleMapper.selectByExample(weekend);
    }
}