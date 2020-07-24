package com.smart4y.cloud.base.infrastructure.mapper;

import com.smart4y.cloud.core.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.dto.OpenAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/24.
 */
@Mapper
@Repository
public interface BaseAuthorityUserCustomMapper {

    /**
     * 获取用户已授权权限完整信息
     */
    List<AuthorityMenuDTO> selectAuthorityMenuByUser(@Param("userId") Long userId);

    /**
     * 获取用户已授权权限
     */
    List<OpenAuthority> selectAuthorityByUser(@Param("userId") Long userId);
}