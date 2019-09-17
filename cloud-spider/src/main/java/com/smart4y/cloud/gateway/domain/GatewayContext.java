package com.smart4y.cloud.gateway.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 网关上下文
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Getter
@Setter
@ToString
public class GatewayContext {

    public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";
    /**
     * cache json body
     */
    private String requestBody;
    /**
     * cache Response Body
     */
    private Object responseBody;
    /**
     * request headers
     */
    private HttpHeaders requestHeaders;
    /**
     * cache form data
     */
    private MultiValueMap<String, String> formData;
    /**
     * cache all request data include:form data and query param
     */
    private MultiValueMap<String, String> allRequestData = new LinkedMultiValueMap<>(0);
}