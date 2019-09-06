package com.smart4y.cloud.hippo.interfaces.feign;

import com.smart4y.cloud.core.ResultBody;
import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 权限控制API 服务
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
public interface BaseAuthorityFeign {

    /**
     * 获取所有访问权限列表
     */
    @GetMapping("/authority/access")
    ResultBody<List<AuthorityResourceDTO>> findAuthorityResource();

    /**
     * 获取菜单权限列表
     */
    @GetMapping("/authority/menu")
    ResultBody<List<AuthorityMenuDTO>> findAuthorityMenu();
}