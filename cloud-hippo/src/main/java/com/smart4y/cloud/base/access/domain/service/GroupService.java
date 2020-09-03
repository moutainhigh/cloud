package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacGroup;
import com.smart4y.cloud.base.access.domain.entity.RbacGroupRole;
import com.smart4y.cloud.base.access.domain.entity.RbacGroupUser;
import com.smart4y.cloud.base.access.infrastructure.persistence.mybatis.RbacGroupMapper;
import com.smart4y.cloud.base.access.infrastructure.persistence.mybatis.RbacGroupRoleMapper;
import com.smart4y.cloud.base.access.infrastructure.persistence.mybatis.RbacGroupUserMapper;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import tk.mybatis.mapper.weekend.Weekend;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao on 2020/8/13 17:23
 */
@DomainService
public class GroupService extends BaseDomainService<RbacGroup> {

    private final RbacGroupMapper rbacGroupMapper;
    private final RbacGroupUserMapper rbacGroupUserMapper;
    private final RbacGroupRoleMapper rbacGroupRoleMapper;

    @Autowired
    public GroupService(RbacGroupUserMapper rbacGroupUserMapper, RbacGroupRoleMapper rbacGroupRoleMapper, RbacGroupMapper rbacGroupMapper) {
        this.rbacGroupUserMapper = rbacGroupUserMapper;
        this.rbacGroupRoleMapper = rbacGroupRoleMapper;
        this.rbacGroupMapper = rbacGroupMapper;
    }

    /**
     * 获取指定组织列表
     *
     * @param groupTypes 组织类型列表
     * @return 组织列表
     */
    public List<RbacGroup> getGroupsByTypes(@NonNull Collection<String> groupTypes) {
        if (CollectionUtils.isEmpty(groupTypes)) {
            return Collections.emptyList();
        }
        Weekend<RbacGroup> weekend = Weekend.of(RbacGroup.class);
        weekend
                .weekendCriteria()
                .andIn(RbacGroup::getGroupType, groupTypes);
        return this.list(weekend);
    }

    /**
     * 获取指定组织的子节点
     * <p>
     * 不包含`人员`
     * </p>
     *
     * @param parentId 父级ID
     * @return 子节点组织列表
     */
    public List<RbacGroup> getChildrenByParentId(long parentId) {
        Weekend<RbacGroup> weekend = Weekend.of(RbacGroup.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroup::getGroupParentId, parentId)
                .andNotEqualTo(RbacGroup::getGroupType, "u");
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
        if (CollectionUtils.isEmpty(groupIds)) {
            return Collections.emptyList();
        }
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

    /**
     * 移除组织关联的角色
     *
     * @param roleIds 角色ID列表
     */
    public void removeRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        Weekend<RbacGroupRole> weekend = Weekend.of(RbacGroupRole.class);
        weekend
                .weekendCriteria()
                .andIn(RbacGroupRole::getRoleId, roleIds);
        rbacGroupRoleMapper.deleteByExample(weekend);
    }

    /**
     * 更新`子节点状态`为`存在子节点`
     *
     * @param groupId 组织ID
     */
    public void modifyChildForExist(long groupId) {
        if (0 == groupId) {
            return;
        }
        RbacGroup record = new RbacGroup()
                .setGroupId(groupId)
                .setExistChild(true)
                .setLastModifiedDate(LocalDateTime.now());
        this.updateSelectiveById(record);
    }

    /**
     * 更新`子节点状态`为`不存在子节点`
     *
     * @param groupId 组织ID
     */
    public void modifyChildForNotExist(long groupId) {
        if (0 == groupId) {
            return;
        }
        RbacGroup record = new RbacGroup()
                .setGroupId(groupId)
                .setExistChild(false)
                .setLastModifiedDate(LocalDateTime.now());
        this.updateSelectiveById(record);
    }

    /**
     * 是否为非空
     * <p>
     * 即当前组织下是否存在子公司/部门/小组/岗位/人员等
     * </p>
     *
     * @param groupId 组织ID
     * @return true非空，false空
     */
    public boolean isNotEmpty(long groupId) {
        Weekend<RbacGroup> weekend = Weekend.of(RbacGroup.class);
        weekend
                .weekendCriteria()
                .andNotEqualTo(RbacGroup::getGroupId, groupId)
                .andLike(RbacGroup::getGroupHierarchyId, "%<" + groupId + "<%");
        weekend
                .setOrderByClause("group_id DESC LIMIT 1");
        return this.exist(weekend);
    }

    /**
     * 转移父组织关系
     *
     * @param groupId       当前组织
     * @param parentGroupId 待转移到的组织下
     */
    public void transferParent(long groupId, long parentGroupId) {
        RbacGroup record = new RbacGroup()
                .setGroupParentId(parentGroupId)
                .setLastModifiedDate(LocalDateTime.now());
        Weekend<RbacGroup> weekend = Weekend.of(RbacGroup.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroup::getGroupParentId, groupId);
        rbacGroupMapper.updateByExampleSelective(record, weekend);

        // TODO 层级ID也要更新
    }

    /**
     * 添加组织用户关联信息
     *
     * @param groupId 组织ID
     * @param userIds 用户ID列表
     */
    public void saveGroupUser(long groupId, Collection<Long> userIds) {
        if (0 == groupId || CollectionUtils.isEmpty(userIds)) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        List<RbacGroupUser> items = userIds.stream()
                .map(userId -> new RbacGroupUser()
                        .setGroupId(groupId)
                        .setUserId(userId)
                        .setCreatedDate(now))
                .collect(Collectors.toList());
        rbacGroupUserMapper.insertList(items);
    }
}