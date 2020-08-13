package com.smart4y.cloud.base.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.entity.RbacRolePrivilege;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 角色权限关联表
 *
 * @author 2020/08/11 15:58 on Youtao
 */
@Mapper
@Repository
public interface RbacRolePrivilegeMapper extends CloudMapper<RbacRolePrivilege> {
}