package com.smart4y.cloud.base.domain.repository;

import com.smart4y.cloud.base.domain.model.BaseRole;
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
     * 获取 超级管理员（admin）权限
     */
    List<OpenAuthority> selectAdminAuthorities();

    /**
     * 获取 应用权限
     */
    List<OpenAuthority> selectAppAuthorities(@Param("appId") String appId);

    /**
     * 获取 角色权限
     */
    List<OpenAuthority> selectRoleAuthorities(@Param("roleId") long roleId);

    /**
     * 获取 角色菜单权限
     */
    List<AuthorityMenuDTO> selectRoleMenuAuthorities(@Param("roleId") long roleId);

    /**
     * 获取 用户菜单权限
     */
    List<AuthorityMenuDTO> selectUserMenuAuthorities(@Param("userId") long userId);

    /**
     * 获取 用户权限
     */
    List<OpenAuthority> selectUserAuthorities(@Param("userId") long userId);

    /**
     * 获取 菜单权限
     */
    List<AuthorityMenuDTO> selectMenuAuthoritiesAll();

    /**
     * 获取 所有资源授权列表
     */
    List<AuthorityResourceDTO> selectAllAuthorityResource();

    /**
     * 获取 用户角色
     */
    List<BaseRole> selectUserRoles(@Param("userId") long userId);
}