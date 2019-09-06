package com.smart4y.cloud.hippo.interfaces.feign;

import com.smart4y.cloud.core.ResultBody;
import com.smart4y.cloud.core.application.dto.AppDTO;
import com.smart4y.cloud.core.application.dto.OpenClientDetailsDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 系统应用信息服务
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
public interface BaseAppFeign {

    /**
     * 获取 应用基础信息
     *
     * @param appId 应用Id
     */
    @GetMapping("/app/{appId}/info")
    ResultBody<AppDTO> getApp(@PathVariable("appId") String appId);

    /**
     * 获取 应用开发配置信息
     *
     * @param clientId 客户端Id
     */
    @GetMapping("/app/client/{clientId}/info")
    ResultBody<OpenClientDetailsDTO> getAppClientInfo(@PathVariable("clientId") String clientId);
}