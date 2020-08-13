package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacRolePrivilege;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao
 * on 2020/8/7 14:46
 */
@DomainService
public class RolePrivilegeService extends BaseDomainService<RbacRolePrivilege> {

    public List<RbacRolePrivilege> getRoles(long roleId, Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacRolePrivilege> weekend = Weekend.of(RbacRolePrivilege.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacRolePrivilege::getRoleId, roleId)
                .andIn(RbacRolePrivilege::getPrivilegeId, privilegeIds);
        return this.list(weekend);
    }

    public List<RbacRolePrivilege> getPrivileges(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacRolePrivilege> weekend = Weekend.of(RbacRolePrivilege.class);
        weekend
                .weekendCriteria()
                .andIn(RbacRolePrivilege::getRoleId, roleIds);
        return this.list(weekend);
    }

    /**
     * 移除权限
     */
    public void removeByPrivilege(Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return;
        }
        Weekend<RbacRolePrivilege> weekend = Weekend.of(RbacRolePrivilege.class);
        weekend
                .weekendCriteria()
                .andIn(RbacRolePrivilege::getPrivilegeId, privilegeIds);
        this.remove(weekend);
    }

    public void add(long roleId, Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        List<RbacRolePrivilege> items = privilegeIds.stream()
                .map(privilegeId -> new RbacRolePrivilege()
                        .setRoleId(roleId)
                        .setPrivilegeId(privilegeId)
                        .setCreatedDate(now)).collect(Collectors.toList());
        this.saveBatch(items);
    }
}