package com.smart4y.cloud.gateway.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.gateway.domain.entity.GatewayIpLimit;
import com.smart4y.cloud.gateway.domain.entity.GatewayIpLimitApi;
import com.smart4y.cloud.gateway.interfaces.dtos.IpLimitQuery;
import com.smart4y.cloud.core.dto.IpLimitApiDTO;

import java.util.List;

/**
 * 网关IP访问控制
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface GatewayIpLimitService {

    /**
     * 分页查询
     */
    PageInfo<GatewayIpLimit> findListPage(IpLimitQuery query);

    /**
     * 查询白名单
     *
     * @return
     */
    List<IpLimitApiDTO> findBlackList();

    /**
     * 查询黑名单
     *
     * @return
     */
    List<IpLimitApiDTO> findWhiteList();

    /**
     * 查询策略已绑定API列表
     *
     * @return
     */
    List<GatewayIpLimitApi> findIpLimitApiList(long policyId);

    /**
     * 获取IP限制策略
     *
     * @param policyId
     * @return
     */
    GatewayIpLimit getIpLimitPolicy(long policyId);

    /**
     * 添加IP限制策略
     *
     * @param policy
     * @return
     */
    GatewayIpLimit addIpLimitPolicy(GatewayIpLimit policy);

    /**
     * 更新IP限制策略
     *
     * @param policy
     */
    GatewayIpLimit updateIpLimitPolicy(GatewayIpLimit policy);

    /**
     * 删除IP限制策略
     *
     * @param policyId
     */
    void removeIpLimitPolicy(long policyId);

    /**
     * 绑定API, 一个API只能绑定一个策略
     *
     * @param policyId
     * @param apis
     */
    void addIpLimitApis(long policyId, List<Long> apis);

    /**
     * 清空绑定的API
     *
     * @param policyId
     */
    void clearIpLimitApisByPolicyId(long policyId);

    /**
     * API解除所有策略
     *
     * @param apiId
     */
    void clearIpLimitApisByApiId(long apiId);
}