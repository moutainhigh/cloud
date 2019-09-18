package com.smart4y.cloud.base.application;

import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
public interface AuthorityService {

    /**
     * 获取 所有访问权限列表
     *
     * @return 所有访问权限列表
     */
    List<AuthorityResourceDTO> getAuthorityResources();

    /**
     * 获取 菜单权限列表
     */
    List<AuthorityMenuDTO> getMenuAuthoritiesAll();
}