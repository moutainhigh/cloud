package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacPrivilege;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Youtao
 * on 2020/8/7 14:51
 */
@DomainService
public class PrivilegeService extends BaseDomainService<RbacPrivilege> {

    public List<RbacPrivilege> getPrivileges(Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilege> weekend = Weekend.of(RbacPrivilege.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilege::getPrivilegeId, privilegeIds);
        return this.list(weekend);
    }

    public List<RbacPrivilege> getByPrivileges(String privilegeType, Collection<String> privileges) {
        if (CollectionUtils.isEmpty(privileges)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilege> weekend = Weekend.of(RbacPrivilege.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacPrivilege::getPrivilegeType, privilegeType)
                .andIn(RbacPrivilege::getPrivilege, privileges);
        return this.list(weekend);
    }

    /**
     * 移除权限
     */
    public void removeByPrivilege(Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return;
        }
        Weekend<RbacPrivilege> weekend = Weekend.of(RbacPrivilege.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilege::getPrivilegeId, privilegeIds);
        this.remove(weekend);
    }
}