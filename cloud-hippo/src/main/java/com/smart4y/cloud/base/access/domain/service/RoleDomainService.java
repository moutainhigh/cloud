package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.model.RbacRole;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Youtao
 * on 2020/8/4 16:16
 */
@DomainService
public class RoleDomainService extends BaseDomainService<RbacRole> {

    public List<RbacRole> getUsers(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacRole> weekend = Weekend.of(RbacRole.class);
        weekend
                .weekendCriteria()
                .andIn(RbacRole::getRoleId, roleIds);
        return this.list(weekend);
    }
}