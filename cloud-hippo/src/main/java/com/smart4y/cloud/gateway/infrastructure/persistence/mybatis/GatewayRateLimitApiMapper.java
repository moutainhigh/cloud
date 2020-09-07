package com.smart4y.cloud.gateway.infrastructure.persistence.mybatis;

import com.smart4y.cloud.gateway.domain.entity.GatewayRateLimitApi;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GatewayRateLimitApiMapper extends CloudMapper<GatewayRateLimitApi> {
}