package com.smart4y.cloud.base.gateway.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.gateway.domain.entity.GatewayRateLimit;
import com.smart4y.cloud.base.gateway.domain.entity.GatewayRateLimitApi;
import com.smart4y.cloud.base.gateway.interfaces.dtos.RateLimitQuery;
import com.smart4y.cloud.core.dto.RateLimitApiDTO;

import java.util.List;

/**
 * 访问日志
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface GatewayRateLimitService {

    /**
     * 分页查询
     */
    PageInfo<GatewayRateLimit> findListPage(RateLimitQuery query);

    /**
     * 查询接口流量限制
     */
    List<RateLimitApiDTO> findRateLimitApiList();

    /**
     * 查询策略已绑定API列表
     *
     * @return
     */
    List<GatewayRateLimitApi> findRateLimitApiList(Long policyId);

    /**
     * 获取限流策略
     *
     * @param policyId
     * @return
     */
    GatewayRateLimit getRateLimitPolicy(Long policyId);

    /**
     * 添加限流策略
     *
     * @param policy
     * @return
     */
    GatewayRateLimit addRateLimitPolicy(GatewayRateLimit policy);

    /**
     * 更新限流策略
     *
     * @param policy
     */
    GatewayRateLimit updateRateLimitPolicy(GatewayRateLimit policy);

    /**
     * 删除限流策略
     *
     * @param policyId
     */
    void removeRateLimitPolicy(Long policyId);

    /**
     * 绑定API, 一个API只能绑定一个策略
     *
     * @param policyId
     * @param apis
     */
    void addRateLimitApis(Long policyId, String... apis);

    /**
     * 清空绑定的API
     *
     * @param policyId
     */
    void clearRateLimitApisByPolicyId(Long policyId);

    /**
     * API解除所有策略
     *
     * @param apiId
     */
    void clearRateLimitApisByApiId(Long apiId);
}
