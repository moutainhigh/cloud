package com.smart4y.cloud.hippo.system.infrastructure.mapper;

import com.smart4y.cloud.hippo.system.domain.model.BaseAccount;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BaseAccountMapper extends CloudMapper<BaseAccount> {
}