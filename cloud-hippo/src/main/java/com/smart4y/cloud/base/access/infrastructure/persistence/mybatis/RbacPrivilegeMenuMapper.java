package com.smart4y.cloud.base.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.entity.RbacPrivilegeMenu;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 权限菜单关联表
 *
 * @author 2020/08/26 16:31 on Youtao
 */
@Mapper
@Repository
public interface RbacPrivilegeMenuMapper extends CloudMapper<RbacPrivilegeMenu> {
}