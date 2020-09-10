package com.smart4y.cloud.hippo.infrastructure.persistence.mybatis;

import com.smart4y.cloud.hippo.domain.entity.RbacGroupRole;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 组织角色关联表
 *
 * @author 2020/09/10 17:53 on Youtao
 */
@Mapper
@Repository
public interface RbacGroupRoleMapper extends CloudMapper<RbacGroupRole> {
}