package com.smart4y.cloud.uaa.infrastructure.feign;

import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.interfaces.OpenClientDetailsDTO;
import com.smart4y.cloud.uaa.infrastructure.feign.fallback.BaseAppFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 系统应用信息服务
 *
 * @author Youtao
 * Created by youtao on 2019-09-06.
 */
@FeignClient(value = BaseConstants.BASE_SERVER, contextId = "baseUserFeign", fallbackFactory = BaseAppFeignFallbackFactory.class)
public interface BaseAppFeign {

    /**
     * 获取 应用开发配置信息
     *
     * @param clientId 客户端Id
     */
    @GetMapping("/app/client/{clientId}/info")
    ResultMessage<OpenClientDetailsDTO> getAppClientInfo(@PathVariable("clientId") String clientId);
}