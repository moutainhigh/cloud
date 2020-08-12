package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.model.RbacOperation;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Youtao on 2020/8/7 14:55
 */
@DomainService
public class OperationService extends BaseDomainService<RbacOperation> {

    /**
     * 失效操作列表
     */
    public List<RbacOperation> getInvalidOperations(String serviceId, Collection<String> validOperationCodes) {
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        WeekendCriteria<RbacOperation, Object> criteria = weekend.weekendCriteria();
        criteria
                .andEqualTo(RbacOperation::getOperationServiceId, serviceId);
        if (CollectionUtils.isNotEmpty(validOperationCodes)) {
            criteria
                    .andNotIn(RbacOperation::getOperationCode, validOperationCodes);
        }
        return this.list(weekend);
    }

    /**
     * 获取操作
     */
    public List<RbacOperation> getOperations(Collection<Long> operationIds) {
        if (CollectionUtils.isEmpty(operationIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        weekend
                .weekendCriteria()
                .andIn(RbacOperation::getOperationId, operationIds);
        return this.list(weekend);
    }

    /**
     * 获取操作
     */
    public Optional<RbacOperation> getByCode(String operationCode) {
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacOperation::getOperationCode, operationCode);
        return Optional.ofNullable(this.getOne(weekend));
    }

    /**
     * 获取操作列表
     */
    public List<RbacOperation> getByCodes(Collection<String> operationCodes) {
        if (CollectionUtils.isEmpty(operationCodes)) {
            return Collections.emptyList();
        }
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        weekend
                .weekendCriteria()
                .andIn(RbacOperation::getOperationCode, operationCodes);
        return this.list(weekend);
    }

    /**
     * 获取操作列表
     */
    public List<RbacOperation> getByServiceId(String serviceId) {
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacOperation::getOperationServiceId, serviceId);
        return this.list(weekend);
    }

    /**
     * 移除操作
     */
    public void removeByOperations(Collection<Long> operationIds) {
        if (CollectionUtils.isEmpty(operationIds)) {
            return;
        }
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        weekend
                .weekendCriteria()
                .andIn(RbacOperation::getOperationId, operationIds);
        this.remove(weekend);
    }
}