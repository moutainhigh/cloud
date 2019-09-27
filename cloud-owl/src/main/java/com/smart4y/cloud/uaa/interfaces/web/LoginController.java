package com.smart4y.cloud.uaa.interfaces.web;

import com.smart4y.cloud.core.domain.ResultEntity;
import com.smart4y.cloud.core.infrastructure.security.OpenHelper;
import com.smart4y.cloud.core.infrastructure.security.OpenUserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@RestController
@Api(tags = "用户认证中心")
public class LoginController extends BaseController {

    @Autowired
    private TokenStore tokenStore;

    /**
     * 获取用户基础信息
     */
    @GetMapping("/current/user")
    @ApiOperation(value = "获取用户基础信息")
    public ResultEntity<OpenUserDetails> getUserProfile() {
        OpenUserDetails user = OpenHelper.getUser();
        return ResultEntity.ok(user);
    }

    /**
     * 获取当前登录用户信息-SSO单点登录
     */
    @GetMapping("/current/user/sso")
    @ApiOperation(value = "获取当前登录用户信息-SSO单点登录", notes = "获取当前登录用户信息-SSO单点登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "principal", value = "凭证", required = true, paramType = "form"),
    })
    public Principal principal(Principal principal) {
        return principal;
    }

    /**
     * 获取用户访问令牌
     * 基于oauth2密码模式登录
     *
     * @return access_token
     */
    @PostMapping("/login/token")
    @ApiOperation(value = "获取用户访问令牌", notes = "基于oauth2密码模式登录,无需签名,返回access_token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录名", required = true, paramType = "form"),
            @ApiImplicitParam(name = "password", value = "登录密码", required = true, paramType = "form")
    })
    public ResultEntity<OAuth2AccessToken> getLoginToken(@RequestParam String username, @RequestParam String password) throws Exception {
        OAuth2AccessToken result = getToken(username, password, null);
        return ResultEntity.ok(result);
    }

    /**
     * 退出移除令牌
     */
    @PostMapping("/logout/token")
    @ApiOperation(value = "退出移除令牌", notes = "退出移除令牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "访问令牌", required = true, paramType = "form")
    })
    public ResultEntity removeToken(@RequestParam String token) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
        return ResultEntity.ok();
    }
}