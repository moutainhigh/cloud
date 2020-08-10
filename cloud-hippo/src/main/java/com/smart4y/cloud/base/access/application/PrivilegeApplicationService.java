package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.model.RbacOperation;
import com.smart4y.cloud.base.access.domain.model.RbacPrivilege;
import com.smart4y.cloud.base.access.interfaces.dtos.privilege.RbacPrivilegePageQuery;
import com.smart4y.cloud.core.message.page.Page;

import java.util.List;

/**
 * @author Youtao on 2020/8/10 12:47
 */
public interface PrivilegeApplicationService {

    /**
     * 系统所有操作权限
     */
    List<RbacOperation> getPrivilegesOperations();

    Page<RbacPrivilege> getPrivilegesPage(RbacPrivilegePageQuery query);
}