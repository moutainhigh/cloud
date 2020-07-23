package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.GatewayRoute;
import com.smart4y.cloud.base.interfaces.vo.GatewayRouteVO;
import com.smart4y.cloud.core.infrastructure.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class GatewayRouteConverter extends AbstractConverter<GatewayRoute, GatewayRouteVO> {

    @Override
    public GatewayRouteVO convert(GatewayRoute source, Map<String, Object> parameters) {
        GatewayRouteVO target = new GatewayRouteVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}