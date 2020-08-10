package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.model.RbacOperation;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Optional;

/**
 * @author Youtao on 2020/8/7 14:55
 */
@DomainService
public class OperationService extends BaseDomainService<RbacOperation> {

    public Optional<RbacOperation> getByCode(String code) {
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacOperation::getOperationCode, code);
        return Optional.ofNullable(this.getOne(weekend));
    }
}