package com.smart4y.cloud.base.domain.repository;

import com.smart4y.cloud.core.domain.OpenAuthority;
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