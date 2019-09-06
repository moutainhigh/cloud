package com.smart4y.cloud.gateway.infrastructure.service.feign;

import com.smart4y.cloud.hippo.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.hippo.interfaces.feign.BaseAppFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * 系统应用信息服务
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVER)
public interface BaseAppServiceClient extends BaseAppFeign {
}