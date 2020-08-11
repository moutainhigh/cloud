package com.smart4y.cloud.base.gateway.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.gateway.domain.model.GatewayRateLimitApi;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GatewayRateLimitApiMapper extends CloudMapper<GatewayRateLimitApi> {
}