package com.smart4y.cloud.gateway.domain.repository;

import com.smart4y.cloud.core.infrastructure.mapper.OpenCloudMapper;
import com.smart4y.cloud.gateway.domain.model.BaseApi;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BaseApiMapper extends OpenCloudMapper<BaseApi> {
}