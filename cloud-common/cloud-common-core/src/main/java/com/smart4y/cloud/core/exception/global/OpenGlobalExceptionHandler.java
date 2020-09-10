package com.smart4y.cloud.core.exception.global;

import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.exception.OpenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理器
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@RestControllerAdvice
public class OpenGlobalExceptionHandler {

    /**
     * 认证异常
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler({AuthenticationException.class})
    public static ResultMessage<Void> authenticationException(Exception ex) {
        return ExceptionHelper.resolveException(ex);
    }

    /**
     * OAuth2异常
     */
    @ExceptionHandler({OAuth2Exception.class, InvalidTokenException.class})
    public static ResultMessage<Void> oauth2Exception(Exception ex) {
        return ExceptionHelper.resolveException(ex);
    }

    /**
     * 自定义异常
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler({OpenException.class})
    public static ResultMessage<Void> openException(Exception ex) {
        return ExceptionHelper.resolveException(ex);
    }

    /**
     * 其他异常
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public static ResultMessage<Void> exception(Exception ex) {
        return ExceptionHelper.resolveException(ex);
    }
}