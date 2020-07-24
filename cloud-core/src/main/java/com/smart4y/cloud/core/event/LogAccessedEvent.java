package com.smart4y.cloud.core.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 网关（日志）事件
 *
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class LogAccessedEvent implements Serializable {

    /**
     * 访问时间
     */
    private LocalDateTime requestTime;
    /**
     * 服务名
     */
    private String serviceId;
    /**
     * 响应状态
     */
    private String httpStatus;
    /**
     * 请求头
     */
    private String headers;
    /**
     * 访问路径
     */
    private String path;
    /**
     * 请求数据
     */
    private String params;
    /**
     * 请求IP
     */
    private String ip;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 客户端标识
     */
    private String userAgent;
    /**
     * 响应时间
     */
    private LocalDateTime responseTime;
    /**
     * 错误信息
     */
    private String error;
    /**
     * 认证信息
     */
    private String authentication;
}