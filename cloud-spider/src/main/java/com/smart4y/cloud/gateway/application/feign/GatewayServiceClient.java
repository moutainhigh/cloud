package com.smart4y.cloud.gateway.application.feign;

import com.smart4y.cloud.hippo.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.hippo.interfaces.feign.GatewayFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * 网关对外信息服务
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVER)
public interface GatewayServiceClient extends GatewayFeign {
}