package com.smart4y.cloud.base.application.converter;

import com.smart4y.cloud.base.domain.model.GatewayRoute;
import com.smart4y.cloud.core.application.dto.GatewayRouteDTO;
import com.smart4y.cloud.core.infrastructure.mapper.base.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Component
public class GatewayRouteConverter extends AbstractConverter<GatewayRoute, GatewayRouteDTO> {

    @Override
    public GatewayRouteDTO convert(GatewayRoute gatewayRoute, Map<String, Object> parameters) {
        return new GatewayRouteDTO()
                .setIsPersist(gatewayRoute.getIsPersist())
                .setStatus(gatewayRoute.getStatus())
                .setPath(gatewayRoute.getPath())
                .setRetryable(gatewayRoute.getRetryable())
                .setRouteDesc(gatewayRoute.getRouteDesc())
                .setRouteId(gatewayRoute.getRouteId())
                .setRouteName(gatewayRoute.getRouteName())
                .setServiceId(gatewayRoute.getServiceId())
                .setStripPrefix(gatewayRoute.getStripPrefix())
                .setUrl(gatewayRoute.getUrl());
    }
}