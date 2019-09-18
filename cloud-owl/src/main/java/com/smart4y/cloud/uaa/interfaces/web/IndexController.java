package com.smart4y.cloud.uaa.interfaces.web;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.smart4y.cloud.core.domain.ResultBody;
import com.smart4y.cloud.uaa.application.impl.GiteeAuthServiceImpl;
import com.smart4y.cloud.uaa.application.impl.QQAuthServiceImpl;
import com.smart4y.cloud.uaa.application.impl.WechatAuthServiceImpl;
import com.smart4y.cloud.uaa.infrastructure.feign.BaseAppFeign;
import com.smart4y.cloud.uaa.infrastructure.feign.BaseDeveloperFeign;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private BaseAppFeign baseAppFeign;
    @Autowired
    private BaseDeveloperFeign baseDeveloperFeign;
    @Autowired
    private QQAuthServiceImpl qqAuthService;
    @Autowired
    private GiteeAuthServiceImpl giteeAuthService;
    @Autowired
    private WechatAuthServiceImpl wechatAuthService;
    @Autowired
    private LoginController loginController;

    /**
     * 欢迎页
     */
    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    /**
     * 登录页
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    /**
     * 确认授权页
     */
    @RequestMapping("/oauth/confirm_access")
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
            } catch (Exception e) {
            }
        }
        return "confirm_access";
    }

    /**
     * 自定义oauth2错误页
     */
    @RequestMapping("/oauth/error")
    @ResponseBody
    public Object handleError(HttpServletRequest request) {
        return request.getAttribute("error");
    }

    /**
     * QQ第三方登录回调
     */
    @GetMapping("/oauth/qq/callback")
    public String qq(@RequestParam(value = "code") String code, @RequestHeader HttpHeaders headers) throws Exception {
        String accessToken = qqAuthService.getAccessToken(code);
        String token = "";
        if (accessToken != null) {
            String openId = qqAuthService.getOpenId(token);
            if (openId != null) {
                baseDeveloperFeign.addDeveloperThirdParty(openId, openId, "qq", "", "");
                token = loginController.getToken(openId, openId, "qq").getValue();
            }
        }
        return "redirect:" + qqAuthService.getLoginSuccessUrl() + "?token=" + token;
    }

    /**
     * 微信第三方登录回调
     */
    @GetMapping("/oauth/wechat/callback")
    public String wechat(@RequestParam(value = "code") String code) throws Exception {
        String accessToken = wechatAuthService.getAccessToken(code);
        String token = "";
        if (accessToken != null) {
            String openId = wechatAuthService.getOpenId(token);
            if (openId != null) {
                baseDeveloperFeign.addDeveloperThirdParty(openId, openId, "wechat", "", "");
                token = loginController.getToken(openId, openId, "wechat").getValue();
            }
        }
        return "redirect:" + wechatAuthService.getLoginSuccessUrl() + "?token=" + token;
    }


    /**
     * 码云第三方登录回调
     */
    @GetMapping("/oauth/gitee/callback")
    public String gitee(@RequestParam(value = "code") String code) throws Exception {
        String accessToken = giteeAuthService.getAccessToken(code);
        String token = "";
        if (accessToken != null) {
            JSONObject userInfo = giteeAuthService.getUserInfo(accessToken, null);
            String openId = userInfo.getString("id");
            String name = userInfo.getString("name");
            String avatar = userInfo.getString("avatar_url");
            if (openId != null) {
                baseDeveloperFeign.addDeveloperThirdParty(openId, openId, "gitee", name, avatar);
                token = loginController.getToken(openId, openId, "gitee").getValue();
            }
        }
        return "redirect:" + giteeAuthService.getLoginSuccessUrl() + "?token=" + token;
    }

    /**
     * 获取第三方登录配置
     */
    @ApiOperation(value = "获取第三方登录配置", notes = "任何人都可访问")
    @GetMapping("/login/config")
    @ResponseBody
    public ResultBody getLoginConfig() {
        Map<String, String> map = Maps.newHashMap();
        map.put("qq", qqAuthService.getAuthorizationUrl());
        map.put("wechat", wechatAuthService.getAuthorizationUrl());
        map.put("gitee", giteeAuthService.getAuthorizationUrl());
        return ResultBody.ok().data(map);
    }
}