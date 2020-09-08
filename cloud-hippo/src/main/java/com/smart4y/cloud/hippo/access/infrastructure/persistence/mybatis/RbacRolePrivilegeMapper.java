package com.smart4y.cloud.hippo.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.hippo.access.domain.entity.RbacRolePrivilege;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 角色权限关联表
 *
 * @author 2020/08/26 16:31 on Youtao
 */
@Mapper
@Repository
public interface RbacRolePrivilegeMapper extends CloudMapper<RbacRolePrivilege> {
}