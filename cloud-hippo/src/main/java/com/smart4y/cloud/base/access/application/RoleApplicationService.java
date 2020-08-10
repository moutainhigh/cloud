package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.model.RbacRole;
import com.smart4y.cloud.base.access.interfaces.dtos.role.RbacRolePageQuery;
import com.smart4y.cloud.core.message.page.Page;

/**
 * @author Youtao on 2020/8/10 16:44
 */
public interface RoleApplicationService {

    Page<RbacRole> getRolesPage(RbacRolePageQuery query);
}