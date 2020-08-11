package com.smart4y.cloud.base.infrastructure.mapper;

import com.smart4y.cloud.base.domain.model.GatewayRoute;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 网关（路由）
 *
 * @author 2020/08/11 16:00 on Youtao
 */
@Mapper
@Repository
public interface GatewayRouteMapper extends CloudMapper<GatewayRoute> {
}