package com.smart4y.cloud.core.exception.handler;

import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.exception.global.ExceptionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * 自定义Oauth2异常提示
 *
 * @author Youtao
 * Created by youtao 019-09-05.
 */
@Slf4j
public class OpenOAuth2WebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<ResultMessage<Void>> translate(Exception e) {
        ResultMessage<Void> resultMessage = ExceptionHelper.resolveException(e);
        return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
    }
}