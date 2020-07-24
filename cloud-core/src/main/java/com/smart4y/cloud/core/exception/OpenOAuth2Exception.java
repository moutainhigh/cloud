package com.smart4y.cloud.core.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart4y.cloud.core.exception.handler.OpenOAuth2ExceptionSerializer;

/**
 * 自定义Oauth2异常提示
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@JsonSerialize(using = OpenOAuth2ExceptionSerializer.class)
public class OpenOAuth2Exception extends org.springframework.security.oauth2.common.exceptions.OAuth2Exception {

    public OpenOAuth2Exception(String msg) {
        super(msg);
    }
}