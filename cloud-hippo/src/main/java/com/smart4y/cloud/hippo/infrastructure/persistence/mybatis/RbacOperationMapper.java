package com.smart4y.cloud.hippo.infrastructure.persistence.mybatis;

import com.smart4y.cloud.hippo.domain.entity.RbacOperation;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 功能操作表
 *
 * @author 2020/09/10 17:53 on Youtao
 */
@Mapper
@Repository
public interface RbacOperationMapper extends CloudMapper<RbacOperation> {
}