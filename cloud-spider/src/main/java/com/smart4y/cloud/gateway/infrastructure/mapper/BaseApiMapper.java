package com.smart4y.cloud.gateway.infrastructure.mapper;

import com.smart4y.cloud.core.mapper.CloudMapper;
import com.smart4y.cloud.gateway.domain.model.BaseApi;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BaseApiMapper extends CloudMapper<BaseApi> {
}