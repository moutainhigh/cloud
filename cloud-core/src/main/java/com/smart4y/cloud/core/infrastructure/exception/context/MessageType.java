package com.smart4y.cloud.core.infrastructure.exception.context;

import com.smart4y.cloud.core.domain.IMessageType;
import lombok.Getter;

/**
 * 系统定义异常
 *
 * @author Youtao
 * on 2020/3/18 17:14
 */
public enum MessageType implements IMessageType<String> {

    /**
     * 成功
     * <p>
     * OK
     * </p>
     */
    OK("200", "common.ok"),
    /**
     * 请求失败
     * <p>
     * Bad Request
     * </p>
     */
    BAD_REQUEST("400", "common.bad_request"),
    /**
     * 未授权
     * <p>
     * Unauthorized
     * </p>
     */
    UNAUTHORIZED("401", "common.unauthorized"),
    /**
     * 禁止访问
     * <p>
     * Forbidden
     * </p>
     */
    FORBIDDEN("403", "common.forbidden"),
    /**
     * 未找到
     * <p>
     * Not Found
     * </p>
     */
    NOT_FOUND("404", "common.not_found"),
    /**
     * 请求方法不被允许
     * <p>
     * Method Not Allowed
     * </p>
     */
    METHOD_NOT_ALLOWED("405", "common.method_not_allowed"),
    /**
     * 媒体类型不可接受
     * <p>
     * MEDIA TYPE NOT ACCEPTABLE
     * </p>
     */
    MEDIA_TYPE_NOT_ACCEPTABLE("406", "common.media_type_not_acceptable"),
    /**
     * 请求超时
     * <p>
     * Request Timeout
     * </p>
     */
    REQUEST_TIMEOUT("408", "common.request_timeout"),
    /**
     * 不支持的媒体类型
     * <p>
     * Unsupported Media Type
     * </p>
     */
    UNSUPPORTED_MEDIA_TYPE("415", "common.unsupported_media_type"),
    /**
     * 请求太频繁
     * <p>
     * Too Many Requests
     * </p>
     */
    TOO_MANY_REQUESTS("429", "common.too_many_requests"),
    /**
     * 服务内部错误
     * <p>
     * Internal Server Error
     * </p>
     */
    INTERNAL_SERVER_ERROR("500", "common.internal_server_error"),
    /**
     * 服务不可用
     * <p>
     * Service Unavailable
     * </p>
     */
    SERVICE_UNAVAILABLE("503", "common.service_unavailable"),
    /**
     * 访问超时
     * <p>
     * Gateway Timeout
     * </p>
     */
    GATEWAY_TIMEOUT("504", "common.gateway_timeout"),
    ;

    @Getter
    private final String rtnCode;
    @Getter
    private final String type;

    MessageType(String rtnCode, String type) {
        this.rtnCode = rtnCode;
        this.type = type;
    }
}