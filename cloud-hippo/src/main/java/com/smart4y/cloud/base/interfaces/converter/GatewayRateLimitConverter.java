package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.GatewayRateLimit;
import com.smart4y.cloud.base.interfaces.valueobject.vo.GatewayRateLimitVO;
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
public class GatewayRateLimitConverter extends AbstractConverter<GatewayRateLimit, GatewayRateLimitVO> {

    @Override
    public GatewayRateLimitVO convert(GatewayRateLimit source, Map<String, Object> parameters) {
        GatewayRateLimitVO target = new GatewayRateLimitVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(source.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        target.setLastModifiedDate(source.getLastModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return target;
    }
}