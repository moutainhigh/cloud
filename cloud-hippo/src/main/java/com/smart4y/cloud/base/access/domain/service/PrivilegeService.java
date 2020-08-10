package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.model.RbacPrivilege;
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
}