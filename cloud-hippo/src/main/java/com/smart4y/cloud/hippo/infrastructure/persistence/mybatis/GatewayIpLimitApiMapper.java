package com.smart4y.cloud.hippo.infrastructure.persistence.mybatis;

import com.smart4y.cloud.hippo.domain.entity.GatewayIpLimitApi;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GatewayIpLimitApiMapper extends CloudMapper<GatewayIpLimitApi> {
}