package com.smart4y.cloud.base.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.model.RbacGroup;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 组织表
 *
 * @author 2020/08/11 15:58 on Youtao
 */
@Mapper
@Repository
public interface RbacGroupMapper extends CloudMapper<RbacGroup> {
}