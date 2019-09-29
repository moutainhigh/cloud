package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.GatewayIpLimitApi;
import com.smart4y.cloud.base.interfaces.valueobject.vo.GatewayIpLimitApiVO;
import com.smart4y.cloud.core.infrastructure.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
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