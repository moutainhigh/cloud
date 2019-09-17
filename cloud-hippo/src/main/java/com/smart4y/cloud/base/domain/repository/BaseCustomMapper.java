package com.smart4y.cloud.base.domain.repository;

import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;
import com.smart4y.cloud.core.domain.model.OpenAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Mapper
@Repository
public interface BaseCustomMapper {

    /**
     * 获取 应用已授权权限
     */
    List<OpenAuthority> selectAppAuthorities(@Param("appId") String appId);

    /**
     * 获取 所有资源授权列表
     */
    List<AuthorityResourceDTO> selectAllAuthorityResource();

    /**
     * 获取 菜单权限
     */
    List<AuthorityMenuDTO> selectAuthorityMenu(@Param("status") Integer status);
}