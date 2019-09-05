package com.smart4y.cloud.gateway.infrastructure.service.feign;

import com.smart4y.cloud.core.BaseApp;
import com.smart4y.cloud.core.OpenClientDetails;
import com.smart4y.cloud.core.ResultBody;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Component
public class BaseAppServiceClient {

    /**
     * 获取应用基础信息
     *
     * @param appId 应用Id
     */
    @GetMapping("/app/{appId}/info")
    public ResultBody<BaseApp> getApp(@PathVariable("appId") String appId) {
        // TODO
        return ResultBody.ok();
    }

    /**
     * 获取应用开发配置信息
     *
     * @param clientId
     */
    @GetMapping("/app/client/{clientId}/info")
    public ResultBody<OpenClientDetails> getAppClientInfo(@PathVariable("clientId") String clientId) {
        // TODO
        return ResultBody.ok();
    }
}