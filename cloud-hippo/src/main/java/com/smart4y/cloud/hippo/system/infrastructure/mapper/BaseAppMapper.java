package com.smart4y.cloud.hippo.system.infrastructure.mapper;

import com.smart4y.cloud.hippo.system.domain.model.BaseApp;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BaseAppMapper extends CloudMapper<BaseApp> {
}