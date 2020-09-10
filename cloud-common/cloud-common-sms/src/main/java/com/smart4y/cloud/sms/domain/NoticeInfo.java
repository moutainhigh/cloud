package com.smart4y.cloud.sms.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

/**
 * 通知信息
 */
@Data
@ApiModel(value = "NoticeInfo", description = "通知信息")
public class NoticeInfo implements Serializable {

    /**
     * 通知内容
     */
    @NotNull(message = "通知内容 必填")
    @ApiModelProperty(value = "通知内容", required = true)
    private NoticeData noticeData;

    /**
     * 号码列表
     */
    @NotEmpty(message = "号码列表 必填")
    @ApiModelProperty(value = "号码列表", required = true)
    private Collection<String> phones;
}