package com.smart4y.cloud.hippo.access.interfaces.dtos.route;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/31 10:44
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CreateRouteCommand", description = "路由:添加")
public class CreateRouteCommand implements Serializable {

    @NotBlank(message = "路由名 必填")
    @ApiModelProperty(value = "路由名", required = true)
    private String routeDesc;

    @NotBlank(message = "元素标识 必填")
    @ApiModelProperty(value = "元素标识", required = true)
    private String routeName;

    @NotBlank(message = "负载均衡地址 必填")
    @ApiModelProperty(value = "负载均衡地址", required = true)
    private String routeServiceId;

    @NotBlank(message = "反向代理地址 必填")
    @ApiModelProperty(value = "反向代理地址", required = true)
    private String routeUrl;

    @NotBlank(message = "路由前缀 必填")
    @ApiModelProperty(value = "路由前缀", required = true)
    private String routePath;

    @NotNull(message = "忽略前缀 必填")
    @ApiModelProperty(value = "忽略前缀", required = true)
    private Boolean routeStripPrefix;

    @NotNull(message = "错误重试 必填")
    @ApiModelProperty(value = "错误重试", required = true)
    private Boolean routeRetryable;

    @NotBlank(message = "元素状态 必填")
    @ApiModelProperty(value = "元素状态", allowableValues = "10,20,30", required = true)
    private String routeState;
}