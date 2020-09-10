package com.smart4y.cloud.sms.supplier.upyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collection;

/**
 * 发送响应
 */
@Data
public class UpyunSendResult {

    /**
     * 所有手机号发送短信的结果
     */
    @JsonProperty("message_ids")
    private Collection<MessageId> messageIds;
}
