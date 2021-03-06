package com.smart4y.cloud.core.exception;

import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.message.IMessageType;

/**
 * 未授权异常
 *
 * @author Youtao
 * on 2020/7/21 18:03
 */
public class UnauthorizedException extends OpenException {

    public UnauthorizedException() {
        super(MessageType.UNAUTHORIZED);
    }

    public UnauthorizedException(String rtnCode, String messageText) {
        super(rtnCode, messageText);
    }

    public UnauthorizedException(IMessageType<String> messageType, Object... arguments) {
        super(messageType, arguments);
    }
}