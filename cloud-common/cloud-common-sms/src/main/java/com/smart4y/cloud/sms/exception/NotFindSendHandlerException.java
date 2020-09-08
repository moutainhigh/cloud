package com.smart4y.cloud.sms.exception;

import java.util.Locale;

/**
 * 未找到有效的短信发送处理
 */
public class NotFindSendHandlerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "未找到有效的短信发送处理程序";
        } else {
            DEFAULT_MSG = "Not found effective sms send handler.";
        }
    }

    /**
     * 未找到有效的短信发送处理
     */
    public NotFindSendHandlerException() {
        super(DEFAULT_MSG);
    }
}
