package com.smart4y.cloud.base.infrastructure.mapper;

import com.smart4y.cloud.core.dto.OpenAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BaseAuthorityAppCustomMapper {

    /**
     * 获取应用已授权权限
     */
    List<OpenAuthority> selectAuthorityByApp(@Param("appId") String appId);
}