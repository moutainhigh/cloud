package com.smart4y.cloud.base.infrastructure.mapper;

import com.smart4y.cloud.core.dto.AuthorityApiDTO;
import com.smart4y.cloud.core.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.dto.AuthorityResourceDTO;
import com.smart4y.cloud.core.dto.OpenAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/24.
 */
@Mapper
@Repository
public interface BaseAuthorityCustomMapper {

    /**
     * 查询所有资源授权列表
     */
    List<AuthorityResourceDTO> selectAllAuthorityResource();

    /**
     * 获取菜单权限
     */
    List<AuthorityMenuDTO> selectAuthorityMenu(Map map);

    /**
     * 获取API权限
     */
    List<AuthorityApiDTO> selectAuthorityApi(Map map);

    /**
     * 查询已授权权限列表
     */
    List<OpenAuthority> selectAuthorityAll(Map map);
}