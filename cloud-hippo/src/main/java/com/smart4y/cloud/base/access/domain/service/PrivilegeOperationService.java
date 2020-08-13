package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacPrivilegeOperation;
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

    /**
     * 获取权限对应的操作列表
     *
     * @param privilegeIds 权限ID列表
     * @return 操作列表
     */
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

    /**
     * 获取操作对应的权限列表
     *
     * @param operationIds 操作ID列表
     * @return 权限列表
     */
    public List<RbacPrivilegeOperation> getPrivileges(Collection<Long> operationIds) {
        if (CollectionUtils.isEmpty(operationIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilegeOperation> weekend = Weekend.of(RbacPrivilegeOperation.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilegeOperation::getOperationId, operationIds);
        return this.list(weekend);
    }

    /**
     * 移除权限
     */
    public void removeByPrivilege(Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return;
        }
        Weekend<RbacPrivilegeOperation> weekend = Weekend.of(RbacPrivilegeOperation.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilegeOperation::getPrivilegeId, privilegeIds);
        this.remove(weekend);
    }
}