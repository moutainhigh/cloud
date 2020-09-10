package com.smart4y.cloud.core.message.enums;

import com.smart4y.cloud.core.message.IMessageType;
import lombok.Getter;

/**
 * 系统定义异常
 *
 * @author Youtao
 * on 2020/3/18 17:14
 */
public enum MessageType implements IMessageType<String> {

    // 表示成功
    //GET: 200 OK 请求成功
    //POST: 201 Created 创建成功
    //PUT: 200 OK 更新成功
    //DELETE: 204 No Content 找不到要删除的内容
    // 表示客户端错误
    //400 Bad Request：服务器不理解客户端的请求，未做任何处理
    //401 Unauthorized：用户未提供身份验证凭据，或者没有通过身份验证
    //403 Forbidden：用户通过了身份验证，但是不具有访问资源所需的权限
    //404 Not Found：所请求的资源不存在，或不可用
    //415 Unsupported Media Type：客户端要求的返回格式不支持。比如，API 只能返回 JSON 格式，但是客户端要求返回 XML 格式等
    // 表示服务端错误
    //500 Internal Server Error：客户端请求有效，服务器处理时发生了意外
    //503 Service Unavailable：服务器无法处理请求，一般用于网站维护状态

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