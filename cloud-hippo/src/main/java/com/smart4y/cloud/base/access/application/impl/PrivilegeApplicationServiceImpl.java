package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.PrivilegeApplicationService;
import com.smart4y.cloud.base.access.domain.model.RbacOperation;
import com.smart4y.cloud.base.access.domain.model.RbacPrivilege;
import com.smart4y.cloud.base.access.domain.model.RbacPrivilegeOperation;
import com.smart4y.cloud.base.access.domain.service.OperationService;
import com.smart4y.cloud.base.access.domain.service.PrivilegeOperationService;
import com.smart4y.cloud.base.access.interfaces.dtos.privilege.RbacPrivilegePageQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao on 2020/8/10 12:47
 */
@Slf4j
@ApplicationService
public class PrivilegeApplicationServiceImpl extends BaseDomainService<RbacPrivilege> implements PrivilegeApplicationService {

    @Autowired
    private PrivilegeOperationService privilegeOperationService;
    @Autowired
    private OperationService operationService;

    @Override
    public Page<RbacPrivilege> getPrivilegesPage(RbacPrivilegePageQuery query) {
        Weekend<RbacPrivilege> weekend = Weekend.of(RbacPrivilege.class);
        WeekendCriteria<RbacPrivilege, Object> criteria = weekend.weekendCriteria();
        if (null != query.getPrivilegeId()) {
            criteria.andLike(RbacPrivilege::getPrivilegeId, "%" + query.getPrivilegeId() + "%");
        }
        if (StringUtils.isNotBlank(query.getPrivilege())) {
            criteria.andLike(RbacPrivilege::getPrivilege, "%" + query.getPrivilege() + "%");
        }
        if (StringUtils.isNotBlank(query.getPrivilegeType())) {
            criteria.andEqualTo(RbacPrivilege::getPrivilegeType, query.getPrivilegeType());
        }
        weekend
                .orderBy("createdDate").desc();

        return this.findPage(weekend, query.getPage(), query.getLimit());
    }

    @Override
    public List<RbacOperation> getPrivilegesOperations() {
        Weekend<RbacPrivilege> privilegeWeekend = Weekend.of(RbacPrivilege.class);
        privilegeWeekend
                .weekendCriteria()
                .andEqualTo(RbacPrivilege::getPrivilegeType, "o");
        List<Long> privilegeIds = this.list(privilegeWeekend).stream()
                .map(RbacPrivilege::getPrivilegeId).collect(Collectors.toList());

        List<Long> operationIds = privilegeOperationService.getOperations(privilegeIds).stream()
                .map(RbacPrivilegeOperation::getOperationId).collect(Collectors.toList());

        return operationService.getOperations(operationIds);
    }
}