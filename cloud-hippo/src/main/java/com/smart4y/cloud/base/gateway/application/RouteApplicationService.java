package com.smart4y.cloud.base.gateway.application;

import com.smart4y.cloud.base.gateway.domain.model.GatewayRoute;
import com.smart4y.cloud.base.gateway.interfaces.dtos.route.RoutePageQuery;
import com.smart4y.cloud.core.message.page.Page;

/**
 * @author Youtao on 2020/8/11 17:09
 */
public interface RouteApplicationService {

    Page<GatewayRoute> getRoutesPage(RoutePageQuery query);
}