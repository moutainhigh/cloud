package com.smart4y.cloud.core.infrastructure.exception;

/**
 * 签名异常
 *
 * @author admin
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