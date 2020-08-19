package com.smart4y.cloud.base.gateway.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.entity.GatewayLog;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 网关（访问日志）
 *
 * @author 2020/08/18 15:27 on Youtao
 */
@Mapper
@Repository
public interface GatewayLogMapper extends CloudMapper<GatewayLog> {
}