package com.smart4y.cloud.base.domain.service;

import com.smart4y.cloud.base.domain.model.GatewayRoute;
import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@DomainService
public class GatewayRouteDomainService extends BaseDomainService<GatewayRoute> {

    public List<GatewayRoute> getEnableRoutes() {
        Weekend<GatewayRoute> weekend = Weekend.of(GatewayRoute.class);
        weekend
                .weekendCriteria()
                .andEqualTo(GatewayRoute::getStatus, BaseConstants.ENABLED);
        return super.list(weekend);
    }
}