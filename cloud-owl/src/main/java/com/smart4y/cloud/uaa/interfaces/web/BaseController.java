package com.smart4y.cloud.uaa.interfaces.web;

import com.alibaba.fastjson.JSONObject;
import com.smart4y.cloud.core.security.oauth2.client.OpenOAuth2ClientDetails;
import com.smart4y.cloud.core.security.oauth2.client.OpenOAuth2ClientProperties;
import com.smart4y.cloud.core.toolkit.web.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/27.
 */
@Component
public abstract class BaseController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OpenOAuth2ClientProperties clientProperties;

    //@Autowired
    //private AuthorizationServerEndpointsConfiguration endpoints;
    ///**
    // * 生成 oauth2 token
    // */
    //OAuth2AccessToken getToken(String userName, String password, String type) throws Exception {
    //    OpenOAuth2ClientDetails clientDetails = clientProperties.getOauth2().get("admin");
    //    // 使用oauth2密码模式登录.
    //    Map<String, String> postParameters = new HashMap<>();
    //    postParameters.put("username", userName);
    //    postParameters.put("password", password);
    //    postParameters.put("client_id", clientDetails.getClientId());
    //    postParameters.put("client_secret", clientDetails.getClientSecret());
    //    postParameters.put("grant_type", "password");
    //    // 添加参数区分,第三方登录
    //    postParameters.put("login_type", type);
    //    return OpenHelper.createAccessToken(endpoints, postParameters);
    //}

    JSONObject getToken(String userName, String password, String type, HttpHeaders headers) {
        OpenOAuth2ClientDetails clientDetails = clientProperties.getOauth2().get("portal");
        String url = WebUtils.getServerUrl(WebUtils.getHttpServletRequest()) + "/oauth/token";
        // 使用oauth2密码模式登录.
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("username", userName);
        postParameters.add("password", password);
        postParameters.add("client_id", clientDetails.getClientId());
        postParameters.add("client_secret", clientDetails.getClientSecret());
        postParameters.add("grant_type", "password");
        // 添加参数区分,第三方登录
        postParameters.add("login_type", type);
        // 使用客户端的请求头,发起请求
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 强制移除 原来的请求头,防止token失效
        headers.remove(HttpHeaders.AUTHORIZATION);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(postParameters, headers);
        return restTemplate.postForObject(url, request, JSONObject.class);
    }
}