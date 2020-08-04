package com.smart4y.cloud.base.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.model.RbacFile;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 文件表
 *
 * @author 2020/08/04 11:05 on Youtao
 */
@Mapper
@Repository
public interface RbacFileMapper extends CloudMapper<RbacFile> {
}