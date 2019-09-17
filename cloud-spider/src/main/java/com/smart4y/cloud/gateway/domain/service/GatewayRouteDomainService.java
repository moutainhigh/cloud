package com.smart4y.cloud.gateway.domain.service;

import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import com.smart4y.cloud.gateway.domain.model.GatewayRoute;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/16.
 */
@DomainService
public class GatewayRouteDomainService extends BaseDomainService<GatewayRoute> {

    /**
     * 查询 所有有效的路由列表
     *
     * @return 所有有效的路由列表
     */
    public List<GatewayRoute> findAllByStatusEnable() {
        Weekend<GatewayRoute> weekend = Weekend.of(GatewayRoute.class);
        weekend
                .weekendCriteria()
                .andEqualTo(GatewayRoute::getStatus, BaseConstants.ENABLED);
        return this.list(weekend);
    }
}