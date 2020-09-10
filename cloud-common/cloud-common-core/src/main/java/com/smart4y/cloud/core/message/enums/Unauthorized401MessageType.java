package com.smart4y.cloud.core.message.enums;

import com.smart4y.cloud.core.message.IMessageType;
import lombok.Getter;

/**
 * 未授权401
 *
 * @author Youtao
 * on 2020/7/22 09:19
 */
public enum Unauthorized401MessageType implements IMessageType<String> {

    /**
     * OAuth2认证错误
     */
    INVALID_TOKEN("401001", "common.unauthorized.invalid_token"),
    INVALID_CLIENT("401002", "common.unauthorized.invalid_client"),
    UNAUTHORIZED_CLIENT("401003", "common.unauthorized.unauthorized_client"),
    INVALID_SCOPE("401004", "common.unauthorized.invalid_scope"),
    INVALID_REQUEST("401005", "common.unauthorized.invalid_request"),
    REDIRECT_URI_MISMATCH("401006", "common.unauthorized.redirect_uri_mismatch"),
    UNSUPPORTED_GRANT_TYPE("401007", "common.unauthorized.unsupported_grant_type"),
    UNSUPPORTED_RESPONSE_TYPE("401008", "common.unauthorized.unsupported_response_type"),
    INVALID_GRANT("401009", "invalid_grant"),
    EXPIRED_TOKEN("401010", "expired_token"),

    /**
     * 账号错误
     */
    CREDENTIALS_EXPIRED("401012", "common.unauthorized.credentials_expired"),
    USERNAME_NOT_FOUND("401013", "common.unauthorized.username_not_found"),
    BAD_CREDENTIALS("401014", "common.unauthorized.bad_credentials"),
    ACCOUNT_DISABLED("401015", "common.unauthorized.account_disabled"),
    ACCOUNT_EXPIRED("401016", "common.unauthorized.account_expired"),
    ACCOUNT_LOCKED("401017", "common.unauthorized.account_locked"),
    ;

    @Getter
    private final String rtnCode;
    @Getter
    private final String type;

    Unauthorized401MessageType(String rtnCode, String type) {
        this.rtnCode = rtnCode;
        this.type = type;
    }
}