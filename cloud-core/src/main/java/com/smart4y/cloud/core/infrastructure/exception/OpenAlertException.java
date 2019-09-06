package com.smart4y.cloud.core.infrastructure.exception;

/**
 * 提示消息异常
 *
 * @author admin
 */
public class OpenAlertException extends OpenException {

    public OpenAlertException() {
    }

    public OpenAlertException(String msg) {
        super(msg);
    }

    public OpenAlertException(int code, String msg) {
        super(code, msg);
    }

    public OpenAlertException(int code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}