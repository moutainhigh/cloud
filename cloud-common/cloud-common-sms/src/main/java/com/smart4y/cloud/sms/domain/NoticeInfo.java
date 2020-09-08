package com.smart4y.cloud.sms.domain;

import lombok.Data;

import java.util.Collection;

/**
 * 通知信息
 */
@Data
public class NoticeInfo {

    /**
     * 通知内容
     */
    private NoticeData noticeData;

    /**
     * 号码列表
     */
    private Collection<String> phones;
}
