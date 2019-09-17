package com.smart4y.cloud.base.application;

import com.smart4y.cloud.core.application.dto.GatewayRouteDTO;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
public interface GatewayService {

    /**
     * 获取 黑名单
     */
    List<IpLimitApiDTO> getBlackList();

    /**
     * 获取 白名单
     */
    List<IpLimitApiDTO> getWhiteList();

    /**
     * 获取 接口流量限制
     */
    List<RateLimitApiDTO> getRateLimitApiList();

    /**
     * 获取 有效的路由列表
     *
     * @return 有效的路由列表
     */
    List<GatewayRouteDTO> getRoutes();
}