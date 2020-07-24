package com.smart4y.cloud.base.interfaces.command.gateway;

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
@ApiModel(value = "UpdateRouteCommand", description = "更新路由")
public class UpdateRouteCommand implements Serializable {

    @NotNull(message = "路径ID必填")
    @ApiModelProperty(value = "路径ID", required = true)
    private Long routeId;

    @NotBlank(message = "路径表达式必填")
    @ApiModelProperty(value = "路径表达式", required = true)
    private String path;

    @NotBlank(message = "路由标识必填")
    @ApiModelProperty(value = "路由标识", required = true)
    private String routeName;

    @NotBlank(message = "路由名称必填")
    @ApiModelProperty(value = "路由名称", required = true)
    private String routeDesc;

    @ApiModelProperty(value = "服务名")
    private String serviceId;

    @ApiModelProperty(value = "转发地址")
    private String url;


    @ApiModelProperty(value = "是否忽略前缀（0-不忽略，1-忽略）", allowableValues = "0,1")
    private Integer stripPrefix = 1;

    @ApiModelProperty(value = "是否支持重试（0-不支持，1-支持）", allowableValues = "0,1")
    private Integer retryable = 0;

    @ApiModelProperty(value = "是否启用（0-禁用，1-启用）", allowableValues = "0,1")
    private Integer status = 1;
}