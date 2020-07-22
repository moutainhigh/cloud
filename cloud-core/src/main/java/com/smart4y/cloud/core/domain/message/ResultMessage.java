package com.smart4y.cloud.core.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart4y.cloud.core.infrastructure.exception.context.MessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 响应结果封装
 *
 * @param <T> 范型
 * @author Youtao on 2020/5/13 14:43
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "ResultMessage", description = "响应结果封装")
public class ResultMessage<T> implements Serializable {

    public static final String RTN_CODE = "rtnCode";
    public static final String MESSAGE = "message";
    public static final String TIMESTAMP = "timestamp";
    public static final String DATA = "data";

    /**
     * 响应码
     * <p>
     * 当`rtnCode=200`时表示请求成功，其他值表示请求异常
     * </p>
     */
    @ApiModelProperty(value = "响应码（当`rtnCode=200`时表示请求成功，其他值表示请求异常）", required = true)
    private String rtnCode;

    /**
     * 响应消息
     */
    @ApiModelProperty(value = "响应消息", required = true)
    private String message;

    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据")
    private T data;

    /**
     * 响应时间戳
     * <p>
     * 格式：yyyyMMddHHmmss
     * </p>
     */
    @ApiModelProperty(value = "响应时间（格式：yyyyMMddHHmmss）", required = true)
    private String timestamp;

    private ResultMessage(String rtnCode, T data, String timestamp, String message) {
        this.rtnCode = rtnCode;
        this.data = data;
        this.timestamp = timestamp;
        this.message = message;
    }

    /**
     * 成功
     *
     * @param <T> 范型
     * @return ResultEntity
     */
    public static <T> ResultMessage<T> ok() {
        return restResponse(MessageType.OK.getRtnCode(), null, "OK");
    }

    /**
     * 成功
     *
     * @param data 响应数据
     * @param <T>  范型
     * @return ResultMessage
     */
    public static <T> ResultMessage<T> ok(T data) {
        return restResponse(MessageType.OK.getRtnCode(), data, "OK");
    }

    /**
     * 成功
     *
     * @param data    响应数据
     * @param message 响应消息
     * @param <T>     范型
     * @return ResultMessage
     */
    public static <T> ResultMessage<T> ok(T data, String message) {
        return restResponse(MessageType.OK.getRtnCode(), data, message);
    }

    /**
     * 成功
     *
     * @param rtnCode 响应码
     * @param data    响应数据
     * @param message 响应消息
     * @param <T>     范型
     * @return ResultMessage
     */
    public static <T> ResultMessage<T> ok(String rtnCode, T data, String message) {
        return restResponse(rtnCode, data, message);
    }

    /**
     * 异常
     *
     * @param <T> 范型
     * @return ResultMessage
     */
    public static <T> ResultMessage<T> fail() {
        return restResponse(MessageType.BAD_REQUEST.getRtnCode(), null, "FAIL");
    }

    /**
     * 异常
     *
     * @param rtnCode     响应码
     * @param messageText 响应消息
     * @param <T>         范型
     * @return ResultMessage
     */
    public static <T> ResultMessage<T> fail(String rtnCode, String messageText) {
        return restResponse(rtnCode, null, messageText);
    }

    /**
     * 异常
     *
     * @param rtnCode     响应码
     * @param data        响应数据
     * @param messageText 响应消息
     * @param <T>         范型
     * @return ResultMessage
     */
    public static <T> ResultMessage<T> fail(String rtnCode, T data, String messageText) {
        return restResponse(rtnCode, data, messageText);
    }

    /**
     * 请求是否成功
     *
     * @return true成功，false异常
     */
    @JsonIgnore
    public boolean isOk() {
        return MessageType.OK.getRtnCode().equals(this.rtnCode);
    }

    /**
     * 请求是否异常
     *
     * @return true异常，false成功
     */
    @JsonIgnore
    public boolean isFail() {
        return !isOk();
    }

    /**
     * 响应封装
     *
     * @param rtnCode     响应码
     * @param data        响应数据
     * @param messageText 响应消息
     * @param <T>         范型
     * @return ResultMessage
     */
    private static <T> ResultMessage<T> restResponse(String rtnCode, T data, String messageText) {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return new ResultMessage<>(rtnCode, data, format, messageText);
    }
}