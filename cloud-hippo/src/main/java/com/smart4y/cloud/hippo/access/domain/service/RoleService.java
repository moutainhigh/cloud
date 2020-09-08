package com.smart4y.cloud.hippo.access.domain.service;

import com.smart4y.cloud.hippo.access.domain.entity.RbacRole;
import com.smart4y.cloud.hippo.access.domain.entity.RbacRolePrivilege;
import com.smart4y.cloud.hippo.access.infrastructure.persistence.mybatis.RbacRolePrivilegeMapper;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao
 * on 2020/8/4 16:16
 */
@DomainService
public class RoleService extends BaseDomainService<RbacRole> {

    /**
     * 超级管理员角色ID
     */
    public static final long ADMIN_ROLE_ID = 1L;

    private final RbacRolePrivilegeMapper rbacRolePrivilegeMapper;

    @Autowired
    public RoleService(RbacRolePrivilegeMapper rbacRolePrivilegeMapper) {
        this.rbacRolePrivilegeMapper = rbacRolePrivilegeMapper;
    }

    /**
     * 获取指定角色列表
     *
     * @param roleIds 角色ID列表
     * @return 角色列表
     */
    public List<RbacRole> getByIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacRole> weekend = Weekend.of(RbacRole.class);
        weekend
                .weekendCriteria()
                .andIn(RbacRole::getRoleId, roleIds);
        weekend
                .orderBy("createdDate").desc();
        return this.list(weekend);
    }

    /**
     * 获取分页列表
     *
     * @param pageNo   页码
     * @param pageSize 页大小
     * @param roleName 名称
     * @param roleCode 标识
     * @return 分页列表
     */
    public Page<RbacRole> getPageLike(int pageNo, int pageSize, String roleName, String roleCode) {
        Weekend<RbacRole> weekend = Weekend.of(RbacRole.class);
        WeekendCriteria<RbacRole, Object> criteria = weekend.weekendCriteria();
        if (StringUtils.isNotBlank(roleName)) {
            criteria.andLike(RbacRole::getRoleName, "%" + roleName + "%");
        }
        if (StringUtils.isNotBlank(roleCode)) {
            criteria.andLike(RbacRole::getRoleCode, "%" + roleCode + "%");
        }
        weekend
                .orderBy("createdDate").desc();
        return this.findPage(weekend, pageNo, pageSize);
    }

    /**
     * 获取指定角色的权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public List<RbacRolePrivilege> getRolePrivilegesByRoleId(long roleId) {
        Weekend<RbacRolePrivilege> weekend = Weekend.of(RbacRolePrivilege.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacRolePrivilege::getRoleId, roleId);
        return rbacRolePrivilegeMapper.selectByExample(weekend);
    }

    /**
     * 获取指定角色的权限列表
     *
     * @param roleIds 角色ID列表
     * @return 权限列表
     */
    public List<RbacRolePrivilege> getRolePrivilegesByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacRolePrivilege> weekend = Weekend.of(RbacRolePrivilege.class);
        weekend
                .weekendCriteria()
                .andIn(RbacRolePrivilege::getRoleId, roleIds);
        return rbacRolePrivilegeMapper.selectByExample(weekend);
    }

    /**
     * 移除所有角色的指定权限
     * <p>
     * 移除已分配给角色的权限列表
     * </p>
     *
     * @param privilegeIds 权限ID列表
     */
    public void removePrivileges(Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return;
        }
        Weekend<RbacRolePrivilege> weekend = Weekend.of(RbacRolePrivilege.class);
        weekend
                .weekendCriteria()
                .andIn(RbacRolePrivilege::getPrivilegeId, privilegeIds);
        rbacRolePrivilegeMapper.deleteByExample(weekend);
    }

    /**
     * 对角色授权
     *
     * @param roleId       角色ID
     * @param privilegeIds 权限ID列表
     */
    public void grantPrivileges(long roleId, Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        List<RbacRolePrivilege> items = privilegeIds.stream()
                .map(privilegeId -> new RbacRolePrivilege()
                        .setRoleId(roleId)
                        .setPrivilegeId(privilegeId)
                        .setCreatedDate(now)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(items)) {
            rbacRolePrivilegeMapper.insertList(items);
        }
    }
}