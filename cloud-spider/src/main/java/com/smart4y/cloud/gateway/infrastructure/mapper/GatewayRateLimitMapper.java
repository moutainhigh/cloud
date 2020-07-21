package com.smart4y.cloud.gateway.infrastructure.mapper;

import com.smart4y.cloud.core.infrastructure.mapper.CloudMapper;
import com.smart4y.cloud.gateway.domain.model.GatewayRateLimit;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GatewayRateLimitMapper extends CloudMapper<GatewayRateLimit> {
}