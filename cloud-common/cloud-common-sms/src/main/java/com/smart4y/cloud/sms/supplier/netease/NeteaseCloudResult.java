package com.smart4y.cloud.sms.supplier.netease;

import lombok.Data;

/**
 * 响应结果
 */
@Data
public class NeteaseCloudResult {

    /**
     * 成功代码
     */
    public static final Integer SUCCESS_CODE = 200;

    /**
     * 请求返回的结果码。
     */
    private int code;

    /**
     * 请求返回的结果码描述。
     */
    private String msg;

    private Long obj;
}
