package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacUserRole;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

/**
 * @author Youtao
 * on 2020/8/7 14:33
 */
@DomainService
public class UserRoleService extends BaseDomainService<RbacUserRole> {

    public List<RbacUserRole> getRoles(long userId) {
        Weekend<RbacUserRole> weekend = Weekend.of(RbacUserRole.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacUserRole::getUserId, userId);
        return this.list(weekend);
    }

    public List<RbacUserRole> getUsers(long roleId) {
        Weekend<RbacUserRole> weekend = Weekend.of(RbacUserRole.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacUserRole::getRoleId, roleId);
        return this.list(weekend);
    }
}