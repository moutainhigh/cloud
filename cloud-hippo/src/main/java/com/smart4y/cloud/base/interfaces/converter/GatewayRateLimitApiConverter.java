package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.GatewayRateLimitApi;
import com.smart4y.cloud.base.interfaces.valueobject.vo.GatewayRateLimitApiVO;
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
public class GatewayRateLimitApiConverter extends AbstractConverter<GatewayRateLimitApi, GatewayRateLimitApiVO> {

    @Override
    public GatewayRateLimitApiVO convert(GatewayRateLimitApi source, Map<String, Object> parameters) {
        GatewayRateLimitApiVO target = new GatewayRateLimitApiVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(source.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        target.setLastModifiedDate(source.getLastModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return target;
    }
}