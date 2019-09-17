package com.smart4y.cloud.base.domain.repository;

import com.smart4y.cloud.base.domain.model.BaseDeveloper;
import com.smart4y.cloud.core.infrastructure.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BaseDeveloperMapper extends CloudMapper<BaseDeveloper> {
}