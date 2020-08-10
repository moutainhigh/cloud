package com.smart4y.cloud.base.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.model.RbacGroupRole;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 组织角色关联表
 *
 * @author 2020/08/10 10:52 on Youtao
 */
@Mapper
@Repository
public interface RbacGroupRoleMapper extends CloudMapper<RbacGroupRole> {
}