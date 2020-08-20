package com.smart4y.cloud.base.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.entity.RbacPrivilege;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 权限表
 *
 * @author 2020/08/20 14:47 on Youtao
 */
@Mapper
@Repository
public interface RbacPrivilegeMapper extends CloudMapper<RbacPrivilege> {
}