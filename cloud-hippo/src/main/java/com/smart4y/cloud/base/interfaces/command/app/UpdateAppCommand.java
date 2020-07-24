package com.smart4y.cloud.base.interfaces.command.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/23 10:04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "UpdateAppCommand", description = "更新应用信息")
public class UpdateAppCommand implements Serializable {

    @NotBlank(message = "应用ID必填")
    @ApiModelProperty(value = "应用ID", required = true)
    private String appId;

    @NotBlank(message = "应用名称必填")
    @ApiModelProperty(value = "应用名称", required = true)
    private String appName;

    @NotBlank(message = "应用英文名称必填")
    @ApiModelProperty(value = "应用英文名称", required = true)
    private String appNameEn;

    @NotBlank(message = "应用类型必填")
    @ApiModelProperty(value = "应用类型（server-应用服务，app-手机应用，pc-PC网页应用，wap-手机网页应用）", allowableValues = "server,app,pc-PC,wap", required = true)
    private String appType;

    @ApiModelProperty(value = "应用图标")
    private String appIcon;

    @ApiModelProperty(value = "手机应用操作系统", allowableValues = "ios/android")
    private String appOs;

    @ApiModelProperty("应用说明")
    private String appDesc;

    @ApiModelProperty(value = "是否启用（0-禁用，1-启用）", allowableValues = "0,1")
    private Integer status = 1;

    @ApiModelProperty("官网地址")
    private String website;

    @ApiModelProperty("开发者")
    private Long developerId;
}