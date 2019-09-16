package com.smart4y.cloud.gateway.domain.repository;

import org.springframework.stereotype.Repository;

/**
 * 网关（路由） 仓储
 *
 * @author Youtao
 *         Created by youtao on 2019-09-09.
 */
@Repository
public interface GatewayRouteRepository {
    //
    //default List<GatewayRouteJpa> findAllByStatus() {
    //    List<GatewayRouteJpa> result = this.findAllByStatusEquals(BaseConstants.ENABLED);
    //    if (CollectionUtils.isEmpty(result)) {
    //        return Collections.emptyList();
    //    }
    //    return result;
    //}
    //
    //default List<RateLimitApiObj> findRateLimitApis() {
    //    List<Object[]> result = this.findRateLimitApi();
    //    if (CollectionUtils.isEmpty(result)) {
    //        return Collections.emptyList();
    //    }
    //    return result.stream()
    //            .map(RateLimitApiObj::castEntity)
    //            .collect(Collectors.toList());
    //}
    //
    ///**
    // * 根据状态查询路由列表
    // *
    // * @param status 0无效 1有效 （
    // *               {@link BaseConstants#DISABLED}
    // *               or {@link BaseConstants#ENABLED}）
    // * @return 路由列表
    // */
    //List<GatewayRouteJpa> findAllByStatusEquals(int status);
    //
    ///**
    // * 查询路由限流数据
    // */
    //@Query(value = "" +
    //        "SELECT " +
    //        "        i.policy_id as policyId, " +
    //        "        p.limit_quota as limitQuota, " +
    //        "        p.interval_unit as intervalUnit, " +
    //        "        p.policy_name as policyName, " +
    //        "        a.api_id as apiId, " +
    //        "        a.api_code as apiCode, " +
    //        "        a.api_name as apiName, " +
    //        "        a.api_category as apiCategory, " +
    //        "        a.service_id as serviceId, " +
    //        "        a.path, " +
    //        "        r.url " +
    //        "FROM gateway_rate_limit_api AS i " +
    //        "INNER JOIN gateway_rate_limit AS p ON i.policy_id = p.policy_id " +
    //        "INNER JOIN base_api AS a ON i.api_id = a.api_id " +
    //        "INNER JOIN gateway_route AS r ON a.service_id = r.route_name " +
    //        "WHERE p.policy_type = 'url'", nativeQuery = true)
    //List<Object[]> findRateLimitApi();
}