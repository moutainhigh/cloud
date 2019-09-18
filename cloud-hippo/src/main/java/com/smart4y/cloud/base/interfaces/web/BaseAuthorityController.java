package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.AuthorityService;
import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;
import com.smart4y.cloud.core.domain.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统权限信息管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
@RestController
@Api(tags = "系统权限管理")
public class BaseAuthorityController {

    private final AuthorityService authorityService;

    @Autowired
    public BaseAuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/authority/access")
    @ApiOperation(value = "获取所有访问权限列表", notes = "获取所有访问权限列表")
    public ResultBody<List<AuthorityResourceDTO>> findAuthorityResource() {
        List<AuthorityResourceDTO> result = authorityService.getAuthorityResources();
        return ResultBody.ok().data(result);
    }

    @GetMapping("/authority/menu")
    @ApiOperation(value = "获取菜单权限列表", notes = "获取菜单权限列表")
    public ResultBody<List<AuthorityMenuDTO>> findAuthorityMenu() {
        List<AuthorityMenuDTO> result = authorityService.getMenuAuthoritiesAll();
        return ResultBody.ok().data(result);
    }
}