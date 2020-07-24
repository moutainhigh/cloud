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
public interface BaseAuthorityRoleCustomMapper {

    /**
     * 获取角色菜单权限
     */
    List<AuthorityMenuDTO> selectAuthorityMenuByRole(@Param("roleId") Long roleId);

    /**
     * 获取角色已授权权限
     */
    List<OpenAuthority> selectAuthorityByRole(@Param("roleId") Long roleId);
}