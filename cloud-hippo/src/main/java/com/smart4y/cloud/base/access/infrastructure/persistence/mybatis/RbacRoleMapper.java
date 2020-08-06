package com.smart4y.cloud.base.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.model.RbacRole;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 角色表
 *
 * @author 2020/08/06 16:01 on Youtao
 */
@Mapper
@Repository
public interface RbacRoleMapper extends CloudMapper<RbacRole> {
}