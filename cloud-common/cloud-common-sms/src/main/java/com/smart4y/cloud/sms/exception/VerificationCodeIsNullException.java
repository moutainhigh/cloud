package com.smart4y.cloud.sms.exception;

import java.util.Locale;

/**
 * 手机验证码信息无效
 */
public class VerificationCodeIsNullException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "手机验证码信息无效";
        } else {
            DEFAULT_MSG = "The mobile verification code information is invalid.";
        }
    }

    /**
     * 手机验证码信息无效
     */
    public VerificationCodeIsNullException() {
        super(DEFAULT_MSG);
    }
}
