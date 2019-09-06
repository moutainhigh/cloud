package com.smart4y.cloud.core.infrastructure.exception;

import com.smart4y.cloud.core.ResultBody;
import com.smart4y.cloud.core.infrastructure.toolkit.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义未认证处理
 *
 * @author liuyadu
 */
@Slf4j
public class OpenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        ResultBody resultBody = OpenGlobalExceptionHandler.resolveException(exception, request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        WebUtils.writeJson(response, resultBody);
    }
}