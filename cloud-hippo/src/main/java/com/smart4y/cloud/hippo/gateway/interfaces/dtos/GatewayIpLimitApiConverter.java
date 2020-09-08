package com.smart4y.cloud.hippo.gateway.interfaces.dtos;

import com.smart4y.cloud.hippo.gateway.domain.entity.GatewayIpLimitApi;
import com.smart4y.cloud.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class GatewayIpLimitApiConverter extends AbstractConverter<GatewayIpLimitApi, GatewayIpLimitApiVO> {

    @Override
    public GatewayIpLimitApiVO convert(GatewayIpLimitApi source, Map<String, Object> parameters) {
        GatewayIpLimitApiVO target = new GatewayIpLimitApiVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(toLocalDateTime(source.getCreatedDate()));
        target.setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}