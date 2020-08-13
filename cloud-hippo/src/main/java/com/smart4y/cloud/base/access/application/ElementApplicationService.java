package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.entity.RbacElement;
import com.smart4y.cloud.base.access.interfaces.dtos.element.RbacElementPageQuery;
import com.smart4y.cloud.core.message.page.Page;

/**
 * @author Youtao on 2020/8/10 15:21
 */
public interface ElementApplicationService {

    Page<RbacElement> getElementsPage(RbacElementPageQuery query);
}