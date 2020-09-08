package com.smart4y.cloud.spider.infrastructure.feign;

import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.constant.BaseConstants;
import com.smart4y.cloud.core.dto.OpenClientDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 系统应用信息服务
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVER)
public interface BaseAppFeign {

    /**
     * 获取 应用基础信息
     *
     * @param appId 应用Id
     */
    @GetMapping("/app/{appId}/info")
    ResultMessage<AppDTO> getApp(@PathVariable("appId") String appId);

    /**
     * 获取 应用开发配置信息
     *
     * @param clientId 客户端Id
     */
    @GetMapping("/app/client/{clientId}/info")
    ResultMessage<OpenClientDetailsDTO> getAppClientInfo(@PathVariable("clientId") String clientId);
}