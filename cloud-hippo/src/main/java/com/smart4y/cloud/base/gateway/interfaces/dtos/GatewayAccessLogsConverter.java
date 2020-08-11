package com.smart4y.cloud.base.gateway.interfaces.dtos;

import com.smart4y.cloud.base.gateway.domain.model.GatewayAccessLogs;
import com.smart4y.cloud.mapper.base.AbstractConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public class GatewayAccessLogsConverter extends AbstractConverter<GatewayAccessLogs, GatewayAccessLogsVO> {

    @Override
    public GatewayAccessLogsVO convert(GatewayAccessLogs source, Map<String, Object> parameters) {
        GatewayAccessLogsVO target = new GatewayAccessLogsVO();
        BeanUtils.copyProperties(source, target);
        target
                .setRequestTime(toLocalDateTime(source.getRequestTime()))
                .setResponseTime(toLocalDateTime(source.getResponseTime()))
                .setCreatedDate(toLocalDateTime(source.getCreatedDate()))
                .setLastModifiedDate(toLocalDateTime(source.getLastModifiedDate()));
        return target;
    }
}