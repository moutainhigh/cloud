package com.smart4y.cloud.sms.exception;

import java.util.Locale;

/**
 * 验证失败
 */
public class VerifyFailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "验证失败";
        } else {
            DEFAULT_MSG = "Validation fails";
        }
    }

    /**
     * 验证失败
     */
    public VerifyFailException() {
        super(DEFAULT_MSG);
    }
}
