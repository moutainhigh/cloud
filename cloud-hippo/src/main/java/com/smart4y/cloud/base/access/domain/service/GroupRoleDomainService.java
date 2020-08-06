package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.model.RbacGroupRole;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

/**
 * @author Youtao
 * on 2020/8/4 16:16
 */
@DomainService
public class GroupRoleDomainService extends BaseDomainService<RbacGroupRole> {

    public List<RbacGroupRole> getUsers(long groupId) {
        Weekend<RbacGroupRole> weekend = Weekend.of(RbacGroupRole.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroupRole::getGroupId, groupId);
        return this.list(weekend);
    }
}