package com.smart4y.cloud.core.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart4y.cloud.core.infrastructure.constants.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 响应结果
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@ToString
@SuppressWarnings("unused")
@ApiModel(value = "响应结果")
public class ResultEntity<T> implements Serializable {

    /**
     * 响应编码
     */
    @Getter
    @ApiModelProperty(value = "响应编码:0-请求处理成功")
    private int code = 0;
    /**
     * 提示消息
     */
    @Getter
    @ApiModelProperty(value = "提示消息")
    private String message;
    /**
     * 请求路径
     */
    @Getter
    @ApiModelProperty(value = "请求路径")
    private String path;
    /**
     * 响应数据
     */
    @Getter
    @ApiModelProperty(value = "响应数据")
    private T data;
    /**
     * HTTP状态码
     */
    @Getter
    @ApiModelProperty(value = "HTTP状态码")
    private int httpStatus;
    /**
     * 附加数据
     */
    @Getter
    @ApiModelProperty(value = "附加数据")
    private Map<String, Object> extra;
    /**
     * 响应时间
     */
    @Getter
    @ApiModelProperty(value = "响应时间")
    private long timestamp = System.currentTimeMillis();

    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    public boolean isOk() {
        return this.code == ErrorCode.OK.getCode();
    }

    public ResultEntity() {
        super();
    }

    public static <T> ResultEntity<T> ok() {
        return new ResultEntity<T>()
                .code(ErrorCode.OK.getCode())
                .message(ErrorCode.OK.getMessage());
    }

    public static <T> ResultEntity<T> ok(T data) {
        return new ResultEntity<T>()
                .code(ErrorCode.OK.getCode())
                .message(ErrorCode.OK.getMessage())
                .data(data);
    }

    public static <T> ResultEntity<T> failed() {
        return failed(ErrorCode.FAIL);
    }

    public static <T> ResultEntity<T> failed(ErrorCode errorCode) {
        return failed(errorCode, errorCode.getMessage());
    }

    public static <T> ResultEntity<T> failed(ErrorCode errorCode, String defaultMessage) {
        return new ResultEntity<T>()
                .code(errorCode.getCode())
                .message(defaultMessage);
    }

    /**
     * 错误信息配置
     */
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("error");

    /**
     * 提示信息国际化
     */
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    private static String i18n(String message, String defaultMessage) {
        return resourceBundle.containsKey(message) ? resourceBundle.getString(message) : defaultMessage;
    }

    private ResultEntity<T> code(int code) {
        this.code = code;
        return this;
    }

    private ResultEntity<T> message(String message) {
        this.message = i18n(ErrorCode.getResultEnum(this.code).getMessage(), message);
        return this;
    }

    private ResultEntity<T> data(T data) {
        this.data = data;
        return this;
    }

    public ResultEntity<T> path(String path) {
        this.path = path;
        return this;
    }

    public ResultEntity<T> httpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }
}