package com.smart4y.cloud.spider.infrastructure.exception.global;

import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.exception.global.ExceptionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

/**
 * @author Youtao
 * on 2020/5/7 10:33
 */
@Slf4j
@Component
public class WebFluxErrorAttributes extends DefaultErrorAttributes {

    public static final String CUSTOM_KEY = "GLOBAL_CUSTOM_KEY";

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(request, includeStackTrace);
        ResultMessage<Object> resultMessage = ExceptionHelper.resolveException((Exception) super.getError(request));
        map.put(CUSTOM_KEY, resultMessage);
        return map;
    }
}