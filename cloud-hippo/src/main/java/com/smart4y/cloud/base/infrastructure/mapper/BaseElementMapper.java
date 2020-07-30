package com.smart4y.cloud.base.infrastructure.mapper;

import com.smart4y.cloud.base.domain.model.BaseElement;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BaseElementMapper extends CloudMapper<BaseElement> {
}