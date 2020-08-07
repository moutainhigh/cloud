package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.model.RbacUser;
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
public class UserService extends BaseDomainService<RbacUser> {

    public List<RbacUser> getUsers(Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacUser> weekend = Weekend.of(RbacUser.class);
        weekend
                .weekendCriteria()
                .andIn(RbacUser::getUserId, userIds);
        return this.list(weekend);
    }
}