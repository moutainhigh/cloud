package com.smart4y.cloud.core.exception;

import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.message.IMessageType;

/**
 * 提示消息异常
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
public class OpenAlertException extends OpenException {

    public OpenAlertException() {
        super(MessageType.BAD_REQUEST);
    }

    public OpenAlertException(String messageText) {
        super(MessageType.BAD_REQUEST, messageText);
    }

    public OpenAlertException(IMessageType<String> messageType, Object... arguments) {
        super(messageType, arguments);
    }
}