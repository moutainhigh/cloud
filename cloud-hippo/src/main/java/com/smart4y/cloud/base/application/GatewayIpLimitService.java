package com.smart4y.cloud.base.application;

import com.smart4y.cloud.base.domain.model.GatewayIpLimit;
import com.smart4y.cloud.base.domain.model.GatewayIpLimitApi;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.PageParams;

import java.util.List;

/**
 * 网关IP访问控制
 *
 * @author liuyadu
 */
public interface GatewayIpLimitService {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<GatewayIpLimit> findListPage(PageParams pageParams);

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