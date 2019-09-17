package com.smart4y.cloud.base.application;

import com.smart4y.cloud.core.application.dto.AppDTO;
import com.smart4y.cloud.core.infrastructure.security.OpenClientDetails;

/**
 * 客户端 应用服务
 *
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
public interface AppService {

    /**
     * 获取 客户端详情
     */
    AppDTO getAppInfo(String appId);

    /**
     * 获取app和应用信息
     */
    OpenClientDetails getAppClientInfo(String clientId);
}