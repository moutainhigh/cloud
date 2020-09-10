package com.smart4y.cloud.sms.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 验证信息
 */
@Data
@ApiModel(value = "VerifyInfo", description = "验证信息")
public class VerifyInfo implements Serializable {

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码 必填")
    @ApiModelProperty(value = "手机号码", required = true)
    private String phone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码 必填")
    @ApiModelProperty(value = "验证码", required = true)
    private String code;

    /**
     * 识别码
     */
    @ApiModelProperty(value = "识别码")
    private String identificationCode;
}