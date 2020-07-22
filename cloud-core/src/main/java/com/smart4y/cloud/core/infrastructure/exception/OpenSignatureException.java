package com.smart4y.cloud.core.infrastructure.exception;

import com.smart4y.cloud.core.domain.IMessageType;
import com.smart4y.cloud.core.infrastructure.exception.context.MessageType;

/**
 * 签名异常
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
public class OpenSignatureException extends OpenException {

    public OpenSignatureException() {
        super(MessageType.BAD_REQUEST);
    }

    public OpenSignatureException(String messageText) {
        super(MessageType.BAD_REQUEST, messageText);
    }

    public OpenSignatureException(IMessageType<String> messageType, Object... arguments) {
        super(messageType, arguments);
    }
}