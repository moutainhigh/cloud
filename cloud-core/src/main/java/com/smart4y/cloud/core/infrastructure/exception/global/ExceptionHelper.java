package com.smart4y.cloud.core.infrastructure.exception.global;

import com.smart4y.cloud.core.domain.IMessageType;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.infrastructure.exception.OpenAlertException;
import com.smart4y.cloud.core.infrastructure.exception.OpenException;
import com.smart4y.cloud.core.infrastructure.exception.OpenSignatureException;
import com.smart4y.cloud.core.infrastructure.exception.context.AccessDenied403MessageType;
import com.smart4y.cloud.core.infrastructure.exception.context.MessageType;
import com.smart4y.cloud.core.infrastructure.exception.context.Unauthorized401MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * @author Youtao
 * on 2020/7/21 17:55
 */
@Slf4j
public class ExceptionHelper {

    /**
     * 解析异常
     */
    public static <T> ResultMessage<T> resolveException(Exception e) {

        IMessageType<String> messageType = null;
        String finalRtnCode = null;
        String finalMessage = null;

        String message = e.getMessage();
        String className = e.getClass().getName();

        // ********** ********** ********** ********** ********** **********
        if (className.contains("UsernameNotFoundException")) {
            messageType = Unauthorized401MessageType.USERNAME_NOT_FOUND;
        } else if (className.contains("BadCredentialsException")) {
            messageType = Unauthorized401MessageType.BAD_CREDENTIALS;
        } else if (className.contains("AccountExpiredException")) {
            messageType = Unauthorized401MessageType.ACCOUNT_EXPIRED;
        } else if (className.contains("LockedException")) {
            messageType = Unauthorized401MessageType.ACCOUNT_LOCKED;
        } else if (className.contains("DisabledException")) {
            messageType = Unauthorized401MessageType.ACCOUNT_DISABLED;
        } else if (className.contains("CredentialsExpiredException")) {
            messageType = Unauthorized401MessageType.CREDENTIALS_EXPIRED;
        } else if (className.contains("InvalidClientException")) {
            messageType = Unauthorized401MessageType.INVALID_CLIENT;
        } else if (className.contains("UnauthorizedClientException")) {
            messageType = Unauthorized401MessageType.UNAUTHORIZED_CLIENT;
        } else if (className.contains("InsufficientAuthenticationException")
                || className.contains("AuthenticationCredentialsNotFoundException")) {
            messageType = MessageType.UNAUTHORIZED;
        } else if (className.contains("InvalidGrantException")) {
            messageType = MessageType.UNAUTHORIZED;
            if ("Bad credentials".contains(message)) {
                messageType = Unauthorized401MessageType.BAD_CREDENTIALS;
            } else if ("User is disabled".contains(message)) {
                messageType = Unauthorized401MessageType.ACCOUNT_DISABLED;
            } else if ("User account is locked".contains(message)) {
                messageType = Unauthorized401MessageType.ACCOUNT_LOCKED;
            }
        } else if (className.contains("InvalidScopeException")) {
            messageType = Unauthorized401MessageType.INVALID_SCOPE;
        } else if (className.contains("InvalidTokenException")) {
            messageType = Unauthorized401MessageType.INVALID_TOKEN;
        } else if (className.contains("InvalidRequestException")) {
            messageType = Unauthorized401MessageType.INVALID_REQUEST;
        } else if (className.contains("RedirectMismatchException")) {
            messageType = Unauthorized401MessageType.REDIRECT_URI_MISMATCH;
        } else if (className.contains("UnsupportedGrantTypeException")) {
            messageType = Unauthorized401MessageType.UNSUPPORTED_GRANT_TYPE;
        } else if (className.contains("UnsupportedResponseTypeException")) {
            messageType = Unauthorized401MessageType.UNSUPPORTED_RESPONSE_TYPE;
        }

        // ********** ********** ********** ********** ********** **********
        else if (className.contains("UserDeniedAuthorizationException")) {
            messageType = AccessDenied403MessageType.ACCESS_DENIED;
        } else if (className.contains("AccessDeniedException")) {
            messageType = MessageType.FORBIDDEN;
            if (message.contains("access_denied_black_limited")) {
                messageType = AccessDenied403MessageType.ACCESS_DENIED_BLACK_LIMITED;
            } else if (message.contains("access_denied_white_limited")) {
                messageType = AccessDenied403MessageType.ACCESS_DENIED_WHITE_LIMITED;
            } else if (message.contains("access_denied_authority_expired")) {
                messageType = AccessDenied403MessageType.ACCESS_DENIED_AUTHORITY_EXPIRED;
            } else if (message.contains("access_denied_updating")) {
                messageType = AccessDenied403MessageType.ACCESS_DENIED_UPDATING;
            } else if (message.contains("access_denied_disabled")) {
                messageType = AccessDenied403MessageType.ACCESS_DENIED_DISABLED;
            } else if (message.contains("access_denied_not_open")) {
                messageType = AccessDenied403MessageType.ACCESS_DENIED_NOT_OPEN;
            }
        }

        // ********** ********** ********** ********** ********** **********
        else if (className.contains("HttpMessageNotReadableException")
                || className.contains("TypeMismatchException")
                || className.contains("MissingServletRequestParameterException")) {
            messageType = MessageType.BAD_REQUEST;
        } else if (className.contains("NoHandlerFoundException")) {
            messageType = MessageType.NOT_FOUND;
        } else if (className.contains("HttpRequestMethodNotSupportedException")) {
            messageType = MessageType.METHOD_NOT_ALLOWED;
        } else if (className.contains("HttpMediaTypeNotAcceptableException")) {
            messageType = MessageType.MEDIA_TYPE_NOT_ACCEPTABLE;
        } else if (className.contains("MethodArgumentNotValidException")) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            finalRtnCode = MessageType.BAD_REQUEST.getRtnCode();
            finalMessage = bindingResult.toString();
        } else if (message.equalsIgnoreCase("too_many_requests")) {
            messageType = MessageType.TOO_MANY_REQUESTS;
        } else if (className.contains("IllegalArgumentException")) {
            messageType = MessageType.BAD_REQUEST;
        } else if (className.contains("OpenAlertException")) {
            OpenAlertException openAlertException = (OpenAlertException) e;
            finalRtnCode = openAlertException.getRtnCode();
            finalMessage = openAlertException.getMessage();
        } else if (className.contains("OpenSignatureException")) {
            OpenSignatureException openSignatureException = (OpenSignatureException) e;
            finalRtnCode = openSignatureException.getRtnCode();
            finalMessage = openSignatureException.getMessage();
        } else if (className.contains("OpenException")) {
            OpenException openException = (OpenException) e;
            finalRtnCode = openException.getRtnCode();
            finalMessage = openException.getMessage();
        } else {
            messageType = MessageType.INTERNAL_SERVER_ERROR;
        }

        if (null != messageType) {
            finalRtnCode = messageType.getRtnCode();
            finalMessage = OpenException.getBundleMessageText(messageType, message);
        }
        log.warn(">>>>> >>>>> >>>>> {}", e.getMessage(), e);

        return ResultMessage.fail(finalRtnCode, finalMessage);
    }
}