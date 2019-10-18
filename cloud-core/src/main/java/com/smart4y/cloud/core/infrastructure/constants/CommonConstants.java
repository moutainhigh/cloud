package com.smart4y.cloud.core.infrastructure.constants;

import java.time.format.DateTimeFormatter;

/**
 * 公共常量
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class CommonConstants {

    /**
     * 默认超级管理员账号
     */
    public final static String ROOT = "admin";
    /**
     * 默认最小页码
     */
    public static final int MIN_PAGE = 0;
    /**
     * 最大显示条数
     */
    public static final int MAX_LIMIT = 999;
    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE = 1;
    /**
     * 默认显示条数
     */
    public static final int DEFAULT_LIMIT = 10;
    /**
     * 页码 KEY
     */
    public static final String PAGE_KEY = "page";
    /**
     * 显示条数 KEY
     */
    public static final String PAGE_LIMIT_KEY = "limit";
    /**
     * 排序字段 KEY
     */
    public static final String PAGE_SORT_KEY = "sort";
    /**
     * 排序方向 KEY
     */
    public static final String PAGE_ORDER_KEY = "order";

    /**
     * 客户端ID KEY
     */
    public static final String SIGN_APP_ID_KEY = "APP_ID";
    /**
     * 客户端ID KEY
     */
    public static final String SIGN_CLIENT_ID_KEY = "clientId";
    /**
     * 客户端秘钥 KEY
     */
    public static final String SIGN_SECRET_KEY = "SECRET_KEY";
    /**
     * 客户端秘钥 KEY
     */
    public static final String SIGN_CLIENT_SECRET_KEY = "clientSecret";

    /**
     * 随机字符串 KEY
     */
    public static final String SIGN_NONCE_KEY = "NONCE";
    /**
     * 时间戳 KEY
     */
    public static final String SIGN_TIMESTAMP_KEY = "TIMESTAMP";
    /**
     * 签名类型 KEY
     */
    public static final String SIGN_SIGN_TYPE_KEY = "SIGN_TYPE";
    /**
     * 签名结果 KEY
     */
    public static final String SIGN_SIGN_KEY = "SIGN";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}