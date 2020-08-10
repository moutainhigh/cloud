package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.OperationApplicationService;
import com.smart4y.cloud.base.access.domain.model.RbacOperation;
import com.smart4y.cloud.base.access.interfaces.dtos.operation.RbacOperationPageQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 * @author Youtao on 2020/8/10 16:00
 */
@Slf4j
@ApplicationService
public class OperationApplicationServiceImpl extends BaseDomainService<RbacOperation> implements OperationApplicationService {

    @Override
    public Page<RbacOperation> getOperationsPage(RbacOperationPageQuery query) {
        Weekend<RbacOperation> weekend = Weekend.of(RbacOperation.class);
        WeekendCriteria<RbacOperation, Object> criteria = weekend.weekendCriteria();
        if (StringUtils.isNotBlank(query.getOperationName())) {
            criteria.andLike(RbacOperation::getOperationName, "%" + query.getOperationName() + "%");
        }
        if (StringUtils.isNotBlank(query.getOperationDesc())) {
            criteria.andLike(RbacOperation::getOperationDesc, "%" + query.getOperationDesc() + "%");
        }
        if (StringUtils.isNotBlank(query.getOperationPath())) {
            criteria.andLike(RbacOperation::getOperationPath, "%" + query.getOperationPath() + "%");
        }
        weekend
                .orderBy("createdDate").desc();

        return this.findPage(weekend, query.getPage(), query.getLimit());
    }
}