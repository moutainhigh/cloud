package com.smart4y.cloud.uaa.interfaces.web;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.uaa.application.impl.GiteeAuthServiceImpl;
import com.smart4y.cloud.uaa.application.impl.QQAuthServiceImpl;
import com.smart4y.cloud.uaa.application.impl.WechatAuthServiceImpl;
import com.smart4y.cloud.uaa.infrastructure.feign.BaseAppFeign;
import com.smart4y.cloud.uaa.infrastructure.feign.BaseDeveloperFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Slf4j
@Controller
@Api(tags = "用户授权中心")
public class Oauth2Controller extends BaseController {

    @Autowired
    private QQAuthServiceImpl qqAuthService;
    @Autowired
    private GiteeAuthServiceImpl giteeAuthService;
    @Autowired
    private WechatAuthServiceImpl wechatAuthService;
    @Autowired
    private BaseDeveloperFeign baseDeveloperFeign;
    @Autowired
    private BaseAppFeign baseAppFeign;

    /**
     * 获取第三方登录配置
     */
    @ResponseBody
    @GetMapping("/login/config")
    @ApiOperation(value = "获取第三方登录配置", notes = "任何人都可访问")
    public ResultMessage<Map<String, String>> getLoginConfig() {
        Map<String, String> map = Maps.newHashMap();
        map.put("qq", qqAuthService.getAuthorizationUrl());
        map.put("wechat", wechatAuthService.getAuthorizationUrl());
        map.put("gitee", giteeAuthService.getAuthorizationUrl());
        return ResultMessage.ok(map);
    }

    /**
     * QQ登录回调
     */
    @GetMapping("/oauth/qq/callback")
    @ApiOperation(value = "登录回调（QQ）", notes = "登录回调（QQ）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "Code", required = true, paramType = "form"),
            @ApiImplicitParam(name = "headers", value = "Headers", required = true, paramType = "header")
    })
    public String qq(@RequestParam(value = "code") String code, @RequestHeader HttpHeaders headers) throws Exception {
        String accessToken = qqAuthService.getAccessToken(code);
        String token = "";
        if (accessToken != null) {
            String openId = qqAuthService.getOpenId(token);
            if (openId != null) {
                baseDeveloperFeign.addDeveloperThirdParty(openId, openId, "qq", "", "");
                token = getToken(openId, openId, "qq", headers).getString("access_token");
            }
        }
        return "redirect:" + qqAuthService.getLoginSuccessUrl() + "?token=" + token;
    }

    /**
     * 微信登录回调
     */
    @GetMapping("/oauth/wechat/callback")
    @ApiOperation(value = "登录回调（微信）", notes = "登录回调（微信）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "Code", required = true, paramType = "form"),
    })
    public String wechat(@RequestParam(value = "code") String code, @RequestHeader HttpHeaders headers) throws Exception {
        String accessToken = wechatAuthService.getAccessToken(code);
        String token = "";
        if (accessToken != null) {
            String openId = wechatAuthService.getOpenId(token);
            if (openId != null) {
                baseDeveloperFeign.addDeveloperThirdParty(openId, openId, "wechat", "", "");
                token = getToken(openId, openId, "wechat", headers).getString("access_token");
            }
        }
        return "redirect:" + wechatAuthService.getLoginSuccessUrl() + "?token=" + token;
    }

    /**
     * 码云登录回调
     */
    @GetMapping("/oauth/gitee/callback")
    @ApiOperation(value = "登录回调（码云）", notes = "登录回调（码云）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "Code", required = true, paramType = "form"),
    })
    public String gitee(@RequestParam(value = "code") String code, @RequestHeader HttpHeaders headers) throws Exception {
        String accessToken = giteeAuthService.getAccessToken(code);
        String token = "";
        if (accessToken != null) {
            JSONObject userInfo = giteeAuthService.getUserInfo(accessToken, null);
            String openId = userInfo.getString("id");
            String name = userInfo.getString("name");
            String avatar = userInfo.getString("avatar_url");
            if (openId != null) {
                baseDeveloperFeign.addDeveloperThirdParty(openId, openId, "gitee", name, avatar);
                token = getToken(openId, openId, "gitee", headers).getString("access_token");
            }
        }
        return "redirect:" + giteeAuthService.getLoginSuccessUrl() + "?token=" + token;
    }

    /**
     * 确认授权页
     */
    @GetMapping("/oauth/confirm_access")
    @ApiOperation(value = "确认授权", notes = "确认授权")
    public String confirm_access(HttpServletRequest request, HttpSession session, Map model) {
        Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes") : request.getAttribute("scopes"));
        List<String> scopeList = new ArrayList<String>();
        for (String scope : scopes.keySet()) {
            scopeList.add(scope);
        }
        model.put("scopeList", scopeList);
        Object auth = session.getAttribute("authorizationRequest");
        if (auth != null) {
            try {
                AuthorizationRequest authorizationRequest = (AuthorizationRequest) auth;
                ClientDetails clientDetails = baseAppFeign.getAppClientInfo(authorizationRequest.getClientId()).getData();
                model.put("app", clientDetails.getAdditionalInformation());
            } catch (Exception ignored) {
            }
        }
        return "confirm_access";
    }

    /**
     * 自定义Oauth2错误页
     */
    @ResponseBody
    @GetMapping("/oauth/error")
    @ApiOperation(value = "自定义Oauth2错误处理", notes = "自定义Oauth2错误处理")
    public Object handleError(HttpServletRequest request) {
        return request.getAttribute("error");
    }
}