package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.model.RbacRolePrivilege;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Youtao
 * on 2020/8/7 14:46
 */
@DomainService
public class RolePrivilegeService extends BaseDomainService<RbacRolePrivilege> {

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
}