package com.smart4y.cloud.sms.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Map;

/**
 * 通知内容
 */
@Data
@ApiModel(value = "NoticeData", description = "通知内容")
public class NoticeData implements Serializable {

    /**
     * 类型
     */
    @NotBlank(message = "类型 必填")
    @ApiModelProperty(value = "类型", required = true)
    private String type;

    /**
     * 参数列表
     */
    @ApiModelProperty(value = "参数列表")
    private Map<String, String> params;
}