package com.smart4y.cloud.gateway.infrastructure.service.feign;

import com.smart4y.cloud.hippo.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.hippo.interfaces.feign.BaseAuthorityFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * 系统权限信息服务
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVER)
public interface BaseAuthorityServiceClient extends BaseAuthorityFeign {
}