package com.smart4y.cloud.sms.exception;

import java.util.Locale;

/**
 * 手机号无效
 */
public class PhoneIsNullException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "手机号无效";
        } else {
            DEFAULT_MSG = "Invalid phone number.";
        }
    }

    /**
     * 手机号无效
     */
    public PhoneIsNullException() {
        super(DEFAULT_MSG);
    }
}
