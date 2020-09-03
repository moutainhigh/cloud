package com.smart4y.cloud.core.exception.handler;

import com.smart4y.cloud.core.exception.global.ExceptionHelper;
import com.smart4y.cloud.core.toolkit.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义访问拒绝
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class OpenAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
        response.setStatus(HttpStatus.OK.value());
        WebUtils.writeJson(response, ExceptionHelper.resolveException(exception));
    }
}