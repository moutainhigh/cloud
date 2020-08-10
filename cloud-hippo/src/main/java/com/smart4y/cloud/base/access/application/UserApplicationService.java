package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.model.RbacUser;
import com.smart4y.cloud.base.access.interfaces.dtos.user.RbacUserPageQuery;
import com.smart4y.cloud.core.message.page.Page;

/**
 * @author Youtao on 2020/8/10 17:00
 */
public interface UserApplicationService {

    Page<RbacUser> getUsersPage(RbacUserPageQuery query);
}