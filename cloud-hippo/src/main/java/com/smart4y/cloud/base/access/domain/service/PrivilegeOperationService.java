package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.model.RbacPrivilegeOperation;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Youtao on 2020/8/10 12:48
 */
@DomainService
public class PrivilegeOperationService extends BaseDomainService<RbacPrivilegeOperation> {

    public List<RbacPrivilegeOperation> getOperations(Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilegeOperation> weekend = Weekend.of(RbacPrivilegeOperation.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilegeOperation::getPrivilegeId, privilegeIds);
        return this.list(weekend);
    }
}