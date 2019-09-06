package com.smart4y.cloud.core.infrastructure.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 自定义oauth2异常提示
 *
 * @author liuyadu
 */
@JsonSerialize(using = OpenOAuth2ExceptionSerializer.class)
public class OpenOAuth2Exception extends org.springframework.security.oauth2.common.exceptions.OAuth2Exception {

    public OpenOAuth2Exception(String msg) {
        super(msg);
    }
}