package com.smart4y.cloud.base.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/23 10:04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "UpdateAppClientInfoCommand", description = "更新完善应用开发信息")
public class UpdateAppClientInfoCommand implements Serializable {

    @NotBlank(message = "应用ID必填")
    @ApiModelProperty(value = "应用ID", required = true)
    private String appId;

    @NotBlank(message = "授权类型必填")
    @ApiModelProperty(value = "授权类型（多个使用,号隔开）", required = true)
    private String grantTypes;

    @NotBlank(message = "第三方应用授权回调地址必填")
    @ApiModelProperty(value = "第三方应用授权回调地址", required = true)
    private String redirectUrls;

    @NotBlank(message = "用户授权范围必填")
    @ApiModelProperty(value = "用户授权范围（多个使用,号隔开）", required = true)
    private String scopes;

    @NotBlank(message = "用户自动授权范围必填")
    @ApiModelProperty(value = "用户自动授权范围（多个使用,号隔开）", required = true)
    private String autoApproveScopes;

    @NotNull(message = "令牌有效期必填")
    @ApiModelProperty(value = "令牌有效期（秒）", required = true)
    private Integer accessTokenValidity;

    @NotNull(message = "刷新令牌有效期必填")
    @ApiModelProperty(value = "刷新令牌有效期（秒）", required = true)
    private Integer refreshTokenValidity;
}