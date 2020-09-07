package com.smart4y.cloud.gateway.infrastructure.persistence.mybatis;

import com.smart4y.cloud.gateway.domain.entity.GatewayIpLimit;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GatewayIpLimitMapper extends CloudMapper<GatewayIpLimit> {
}