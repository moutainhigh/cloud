package com.smart4y.cloud.base.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.entity.RbacUser;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户表
 *
 * @author 2020/08/20 14:47 on Youtao
 */
@Mapper
@Repository
public interface RbacUserMapper extends CloudMapper<RbacUser> {
}