package com.smart4y.cloud.uaa.interfaces.web;

import com.smart4y.cloud.core.domain.ResultEntity;
import com.smart4y.cloud.core.infrastructure.security.OpenHelper;
import com.smart4y.cloud.core.infrastructure.security.OpenUserDetails;
import com.smart4y.cloud.core.infrastructure.security.oauth2.client.OpenOAuth2ClientDetails;
import com.smart4y.cloud.core.infrastructure.security.oauth2.client.OpenOAuth2ClientProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Api(tags = "用户认证中心")
@RestController
public class LoginController {

    @Autowired
    private OpenOAuth2ClientProperties clientProperties;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AuthorizationServerEndpointsConfiguration endpoints;

    /**
     * 获取用户基础信息
     */
    @ApiOperation(value = "获取用户基础信息")
    @GetMapping("/current/user")
    public ResultEntity<OpenUserDetails> getUserProfile() {
        OpenUserDetails user = OpenHelper.getUser();
        return ResultEntity.ok(user);
    }

    /**
     * 获取当前登录用户信息-SSO单点登录
     */
    @ApiOperation(value = "获取当前登录用户信息-SSO单点登录", notes = "获取当前登录用户信息-SSO单点登录")
    @GetMapping("/current/user/sso")
    public Principal principal(Principal principal) {
        return principal;
    }

    /**
     * 获取用户访问令牌
     * 基于oauth2密码模式登录
     *
     * @param username
     * @param password
     * @return access_token
     */
    @ApiOperation(value = "获取用户访问令牌", notes = "基于oauth2密码模式登录,无需签名,返回access_token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "form"),
            @ApiImplicitParam(name = "password", required = true, value = "登录密码", paramType = "form")
    })
    @PostMapping("/login/token")
    public ResultEntity<OAuth2AccessToken> getLoginToken(@RequestParam String username, @RequestParam String password) throws Exception {
        OAuth2AccessToken result = getToken(username, password, null);
        return ResultEntity.ok(result);
    }

    /**
     * 退出移除令牌
     */
    @ApiOperation(value = "退出移除令牌", notes = "退出移除令牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", required = true, value = "访问令牌", paramType = "form")
    })
    @PostMapping("/logout/token")
    public ResultEntity removeToken(@RequestParam String token) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
        return ResultEntity.ok();
    }

    /**
     * 生成 oauth2 token
     */
    public OAuth2AccessToken getToken(String userName, String password, String type) throws Exception {
        OpenOAuth2ClientDetails clientDetails = clientProperties.getOauth2().get("admin");
        // 使用oauth2密码模式登录.
        Map<String, String> postParameters = new HashMap<>();
        postParameters.put("username", userName);
        postParameters.put("password", password);
        postParameters.put("client_id", clientDetails.getClientId());
        postParameters.put("client_secret", clientDetails.getClientSecret());
        postParameters.put("grant_type", "password");
        // 添加参数区分,第三方登录
        postParameters.put("login_type", type);
        return OpenHelper.createAccessToken(endpoints, postParameters);
    }
}