package com.smart4y.cloud.base.gateway.interfaces.dtos;

import com.smart4y.cloud.base.gateway.domain.model.GatewayRateLimitApi;
import com.smart4y.cloud.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class GatewayRateLimitApiConverter extends AbstractConverter<GatewayRateLimitApi, GatewayRateLimitApiVO> {

    @Override
    public GatewayRateLimitApiVO convert(GatewayRateLimitApi source, Map<String, Object> parameters) {
        GatewayRateLimitApiVO target = new GatewayRateLimitApiVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}