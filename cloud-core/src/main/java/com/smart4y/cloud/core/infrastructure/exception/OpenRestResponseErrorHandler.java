package com.smart4y.cloud.core.infrastructure.exception;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * 自定RestTemplate异常处理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class OpenRestResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) {
        // false表示不管response的status是多少都返回没有错
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) {

    }
}