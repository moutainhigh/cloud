package com.smart4y.cloud.gateway.infrastructure.service;

import org.springframework.web.server.ServerWebExchange;

/**
 * 日志记录接口
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface AccessLogService {

    /**
     * 发送日志
     */
    void sendLog(ServerWebExchange exchange, Exception e);
}