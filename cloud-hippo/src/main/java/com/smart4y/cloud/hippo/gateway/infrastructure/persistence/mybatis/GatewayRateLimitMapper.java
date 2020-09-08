package com.smart4y.cloud.hippo.gateway.infrastructure.persistence.mybatis;

import com.smart4y.cloud.hippo.gateway.domain.entity.GatewayRateLimit;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GatewayRateLimitMapper extends CloudMapper<GatewayRateLimit> {
}