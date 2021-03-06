package com.smart4y.cloud.core.exception.handler;

import com.smart4y.cloud.core.exception.global.ExceptionHelper;
import com.smart4y.cloud.core.toolkit.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义未认证处理
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class OpenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        response.setStatus(HttpStatus.OK.value());
        WebUtils.writeJson(response, ExceptionHelper.resolveException(exception));
    }
}