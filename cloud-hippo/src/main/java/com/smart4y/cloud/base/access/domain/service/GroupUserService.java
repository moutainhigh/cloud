package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacGroupUser;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

/**
 * @author Youtao
 * on 2020/8/4 16:16
 */
@DomainService
public class GroupUserService extends BaseDomainService<RbacGroupUser> {

    public List<RbacGroupUser> getGroups(long userId) {
        Weekend<RbacGroupUser> weekend = Weekend.of(RbacGroupUser.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroupUser::getUserId, userId);
        return this.list(weekend);
    }

    public List<RbacGroupUser> getUsers(long groupId) {
        Weekend<RbacGroupUser> weekend = Weekend.of(RbacGroupUser.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroupUser::getGroupId, groupId);
        return this.list(weekend);
    }
}