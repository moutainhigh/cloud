package com.smart4y.cloud.hippo.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.hippo.access.domain.entity.RbacGroup;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 组织结构表
 *
 * @author 2020/08/26 16:31 on Youtao
 */
@Mapper
@Repository
public interface RbacGroupMapper extends CloudMapper<RbacGroup> {
}