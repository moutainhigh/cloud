package com.smart4y.cloud.base.gateway.application.impl;

import com.smart4y.cloud.base.gateway.application.RouteApplicationService;
import com.smart4y.cloud.base.gateway.domain.model.GatewayRoute;
import com.smart4y.cloud.base.gateway.interfaces.dtos.route.RoutePageQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 * @author Youtao on 2020/8/11 17:11
 */
@Slf4j
@ApplicationService
public class RouteApplicationServiceImpl extends BaseDomainService<GatewayRoute> implements RouteApplicationService {

    @Override
    public Page<GatewayRoute> getRoutesPage(RoutePageQuery query) {
        Weekend<GatewayRoute> weekend = Weekend.of(GatewayRoute.class);
        WeekendCriteria<GatewayRoute, Object> criteria = weekend.weekendCriteria();
        if (StringUtils.isNotBlank(query.getRouteName())) {
            criteria.andLike(GatewayRoute::getRouteName, "%" + query.getRouteName() + "%");
        }
        if (StringUtils.isNotBlank(query.getRoutePath())) {
            criteria.andLike(GatewayRoute::getRoutePath, "%" + query.getRoutePath() + "%");
        }
        weekend
                .orderBy("createdDate").desc();

        return this.findPage(weekend, query.getPage(), query.getLimit());
    }
}