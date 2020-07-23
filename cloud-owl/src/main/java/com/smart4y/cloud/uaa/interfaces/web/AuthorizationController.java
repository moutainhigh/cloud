package com.smart4y.cloud.uaa.interfaces.web;

import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.infrastructure.security.OpenHelper;
import com.smart4y.cloud.core.infrastructure.security.OpenUserDetails;
import com.smart4y.cloud.uaa.interfaces.command.LoginCommand;
import com.smart4y.cloud.uaa.interfaces.command.LogoutCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * @author Youtao on 2019-09-05.
 */
@RestController
@Api(tags = "用户认证中心")
public class AuthorizationController extends BaseController {

    @Autowired
    private TokenStore tokenStore;

    /**
     * 获取用户基础信息
     */
    @GetMapping("/current/user")
    @ApiOperation(value = "获取用户基础信息")
    public ResultMessage<OpenUserDetails> getUserProfile() {
        OpenUserDetails user = OpenHelper.getUser();
        return ResultMessage.ok(user);
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
    public ResultMessage getLoginToken(@RequestBody LoginCommand command, @RequestHeader HttpHeaders httpHeaders) throws Exception {
        Map result = getToken(command.getUsername(), command.getPassword(), null, httpHeaders);
        return ResultMessage.ok(result);
    }

    /**
     * 退出移除令牌
     */
    @PostMapping("/logout/token")
    @ApiOperation(value = "退出移除令牌", notes = "退出移除令牌")
    public ResultMessage<Void> removeToken(@RequestBody LogoutCommand command) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(command.getToken()));
        return ResultMessage.ok();
    }
}