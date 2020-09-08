package com.smart4y.cloud.sms.domain;

import lombok.Data;

import java.util.Map;

/**
 * 通知内容
 */
@Data
public class NoticeData {

    /**
     * 类型
     */
    private String type;

    /**
     * 参数列表
     */
    private Map<String, String> params;
}
