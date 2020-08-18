package com.smart4y.cloud.gateway.infrastructure.mapper;

import com.smart4y.cloud.gateway.domain.model.GatewayRoute;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 网关（路由）
 *
 * @author 2020/08/18 15:29 on Youtao
 */
@Mapper
@Repository
public interface GatewayRouteMapper extends CloudMapper<GatewayRoute> {
}