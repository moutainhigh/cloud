package com.smart4y.cloud.core.infrastructure.exception;

/**
 * 签名异常
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class OpenSignatureException extends OpenException {

    public OpenSignatureException() {
    }

    public OpenSignatureException(String msg) {
        super(msg);
    }

    public OpenSignatureException(int code, String msg) {
        super(code, msg);
    }

    public OpenSignatureException(int code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}