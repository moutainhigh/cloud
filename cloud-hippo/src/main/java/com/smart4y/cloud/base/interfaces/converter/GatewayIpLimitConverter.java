package com.smart4y.cloud.base.interfaces.converter;

import com.smart4y.cloud.base.domain.model.GatewayIpLimit;
import com.smart4y.cloud.base.interfaces.valueobject.vo.GatewayIpLimitVO;
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
public class GatewayIpLimitConverter extends AbstractConverter<GatewayIpLimit, GatewayIpLimitVO> {

    @Override
    public GatewayIpLimitVO convert(GatewayIpLimit source, Map<String, Object> parameters) {
        GatewayIpLimitVO target = new GatewayIpLimitVO();
        BeanUtils.copyProperties(source, target);
        target.setCreatedDate(source.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        target.setLastModifiedDate(source.getLastModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return target;
    }
}