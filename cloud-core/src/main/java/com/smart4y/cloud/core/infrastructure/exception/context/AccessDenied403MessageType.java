package com.smart4y.cloud.core.infrastructure.exception.context;

import com.smart4y.cloud.core.domain.IMessageType;
import lombok.Getter;

/**
 * 禁止访问403
 *
 * @author Youtao
 * on 2020/7/22 10:59
 */
public enum AccessDenied403MessageType implements IMessageType<String> {

    /**
     * 权限不足
     */
    ACCESS_DENIED("403001", "common.access_denied"),
    ACCESS_DENIED_BLACK_LIMITED("403002", "common.access_denied.access_denied_black_limited"),
    ACCESS_DENIED_WHITE_LIMITED("403003", "common.access_denied.access_denied_white_limited"),
    ACCESS_DENIED_AUTHORITY_EXPIRED("403004", "common.access_denied.access_denied_authority_expired"),
    ACCESS_DENIED_UPDATING("403005", "common.access_denied.access_denied_updating"),
    ACCESS_DENIED_DISABLED("403006", "common.access_denied.access_denied_disabled"),
    ACCESS_DENIED_NOT_OPEN("403007", "common.access_denied.access_denied_not_open"),

    ;

    @Getter
    private final String rtnCode;
    @Getter
    private final String type;

    AccessDenied403MessageType(String rtnCode, String type) {
        this.rtnCode = rtnCode;
        this.type = type;
    }
}