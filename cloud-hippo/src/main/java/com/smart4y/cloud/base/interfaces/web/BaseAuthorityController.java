package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.core.domain.ResultBody;
import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
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

    @GetMapping("/authority/access")
    @ApiOperation(value = "获取所有访问权限列表", notes = "获取所有访问权限列表")
    public ResultBody<List<AuthorityResourceDTO>> findAuthorityResource() {
        // TODO 待开发补充内容
        return ResultBody.ok().data(Collections.emptyList());
    }

    @GetMapping("/authority/menu")
    @ApiOperation(value = "获取菜单权限列表", notes = "获取菜单权限列表")
    public ResultBody<List<AuthorityMenuDTO>> findAuthorityMenu() {
        // TODO 待开发补充内容
        return ResultBody.ok().data(Collections.emptyList());
    }
}