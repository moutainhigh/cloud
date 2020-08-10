package com.smart4y.cloud.gateway.infrastructure.mapper;

import com.smart4y.cloud.mapper.CloudMapper;
import com.smart4y.cloud.gateway.domain.model.GatewayRateLimitApi;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GatewayRateLimitApiMapper extends CloudMapper<GatewayRateLimitApi> {
}