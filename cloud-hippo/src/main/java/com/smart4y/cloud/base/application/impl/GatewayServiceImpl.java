package com.smart4y.cloud.base.application.impl;

import com.smart4y.cloud.base.application.GatewayService;
import com.smart4y.cloud.base.application.converter.GatewayRouteConverter;
import com.smart4y.cloud.base.domain.model.GatewayRoute;
import com.smart4y.cloud.base.domain.service.GatewayIpLimitDomainService;
import com.smart4y.cloud.base.domain.service.GatewayRateLimitDomainService;
import com.smart4y.cloud.base.domain.service.GatewayRouteDomainService;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.application.dto.GatewayRouteDTO;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Slf4j
@ApplicationService
public class GatewayServiceImpl implements GatewayService {

    private final GatewayRouteConverter gatewayRouteConverter;
    private final GatewayRouteDomainService gatewayRouteDomainService;
    private final GatewayIpLimitDomainService gatewayIpLimitDomainService;
    private final GatewayRateLimitDomainService gatewayRateLimitDomainService;

    @Autowired
    public GatewayServiceImpl(GatewayRouteDomainService gatewayRouteDomainService, GatewayRouteConverter gatewayRouteConverter, GatewayIpLimitDomainService gatewayIpLimitDomainService, GatewayRateLimitDomainService gatewayRateLimitDomainService) {
        this.gatewayRouteDomainService = gatewayRouteDomainService;
        this.gatewayRouteConverter = gatewayRouteConverter;
        this.gatewayIpLimitDomainService = gatewayIpLimitDomainService;
        this.gatewayRateLimitDomainService = gatewayRateLimitDomainService;
    }

    @Override
    public List<IpLimitApiDTO> getBlackList() {
        return gatewayIpLimitDomainService.getBlackOrWhiteList(0);
    }

    @Override
    public List<IpLimitApiDTO> getWhiteList() {
        return gatewayIpLimitDomainService.getBlackOrWhiteList(1);
    }

    @Override
    public List<RateLimitApiDTO> getRateLimitApiList() {
        return gatewayRateLimitDomainService.getRateLimitApiList();
    }

    @Override
    public List<GatewayRouteDTO> getRoutes() {
        List<GatewayRoute> enableRoutes = gatewayRouteDomainService.getEnableRoutes();
        return gatewayRouteConverter.convertToList(enableRoutes);
    }
}