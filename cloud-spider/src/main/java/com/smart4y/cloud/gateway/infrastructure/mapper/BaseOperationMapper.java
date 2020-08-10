package com.smart4y.cloud.gateway.infrastructure.mapper;

import com.smart4y.cloud.mapper.CloudMapper;
import com.smart4y.cloud.gateway.domain.model.BaseOperation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BaseOperationMapper extends CloudMapper<BaseOperation> {
}