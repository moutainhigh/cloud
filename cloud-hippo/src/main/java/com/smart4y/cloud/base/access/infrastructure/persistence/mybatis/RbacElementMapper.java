package com.smart4y.cloud.base.access.infrastructure.persistence.mybatis;

import com.smart4y.cloud.base.access.domain.entity.RbacElement;
import com.smart4y.cloud.mapper.CloudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 页面元素表（对应页面按钮）
 *
 * @author 2020/08/20 14:47 on Youtao
 */
@Mapper
@Repository
public interface RbacElementMapper extends CloudMapper<RbacElement> {
}