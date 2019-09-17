package com.smart4y.cloud.base.domain.repository;

import com.smart4y.cloud.base.domain.model.BaseApp;
import com.smart4y.cloud.core.infrastructure.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BaseAppMapper extends CloudMapper<BaseApp> {
}