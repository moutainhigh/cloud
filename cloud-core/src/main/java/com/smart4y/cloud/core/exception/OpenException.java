package com.smart4y.cloud.core.exception;


import com.smart4y.cloud.core.ErrorCode;

/**
 * 基础错误异常
 *
 * @author admin
 */
public class OpenException extends RuntimeException {

    private int code = ErrorCode.ERROR.getCode();

    public OpenException() {
    }

    public OpenException(String msg) {
        super(msg);
    }

    public OpenException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public OpenException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
