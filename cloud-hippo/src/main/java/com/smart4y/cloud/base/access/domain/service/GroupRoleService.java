package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacGroupRole;
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
public class GroupRoleService extends BaseDomainService<RbacGroupRole> {

    public List<RbacGroupRole> getRoles(Collection<Long> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacGroupRole> weekend = Weekend.of(RbacGroupRole.class);
        weekend
                .weekendCriteria()
                .andIn(RbacGroupRole::getGroupId, groupIds);
        return this.list(weekend);
    }

    public List<RbacGroupRole> getRoles(long groupId) {
        Weekend<RbacGroupRole> weekend = Weekend.of(RbacGroupRole.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroupRole::getGroupId, groupId);
        return this.list(weekend);
    }
}