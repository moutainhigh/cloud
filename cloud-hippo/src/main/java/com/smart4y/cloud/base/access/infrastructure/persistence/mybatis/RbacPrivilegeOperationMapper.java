package com.smart4y.cloud.base.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.model.RbacPrivilegeOperation;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 权限操作关联表
 *
 * @author 2020/08/10 15:26 on Youtao
 */
@Mapper
@Repository
public interface RbacPrivilegeOperationMapper extends CloudMapper<RbacPrivilegeOperation> {
}