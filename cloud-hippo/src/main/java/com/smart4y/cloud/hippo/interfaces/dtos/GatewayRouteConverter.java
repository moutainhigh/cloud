package com.smart4y.cloud.hippo.interfaces.dtos;

import com.smart4y.cloud.hippo.domain.entity.GatewayRoute;
import com.smart4y.cloud.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 * Created by youtao on 2019/9/27.
 */
@Component
public class GatewayRouteConverter extends AbstractConverter<GatewayRoute, GatewayRouteVO> {

    @Override
    public GatewayRouteVO convert(GatewayRoute source, Map<String, Object> parameters) {
        GatewayRouteVO target = new GatewayRouteVO();
        BeanUtils.copyProperties(source, target);
        target
                .setPath(source.getRoutePath())
                .setServiceId(source.getRouteServiceId())
                .setUrl(source.getRouteUrl())
                .setStripPrefix(source.getRouteStripPrefix() ? 1 : 0)
                .setRetryable(source.getRouteRetryable() ? 1 : 0)
                .setStatus("10".equals(source.getRouteState()) ? 1 : 0)
                .setCreatedDate(toLocalDateTime(source.getCreatedDate()))
                .setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}