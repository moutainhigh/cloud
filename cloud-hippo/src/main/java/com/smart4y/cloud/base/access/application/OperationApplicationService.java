package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.entity.RbacOperation;
import com.smart4y.cloud.base.access.interfaces.dtos.operation.RbacOperationPageQuery;
import com.smart4y.cloud.core.message.page.Page;

/**
 * @author Youtao on 2020/8/10 15:59
 */
public interface OperationApplicationService {

    Page<RbacOperation> getOperationsPage(RbacOperationPageQuery query);
}