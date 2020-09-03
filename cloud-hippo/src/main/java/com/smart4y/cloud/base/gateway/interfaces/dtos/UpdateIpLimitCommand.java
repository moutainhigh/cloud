package com.smart4y.cloud.base.gateway.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao on 2019/9/25.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "UpdateIpLimitCommand", description = "更新IP策略")
public class UpdateIpLimitCommand implements Serializable {

    @NotNull(message = "策略ID 必填")
    @ApiModelProperty(value = "策略ID", required = true)
    private Long policyId;

    @NotBlank(message = "策略名称 必填")
    @ApiModelProperty(value = "策略名称", required = true)
    private String policyName;

    @NotNull(message = "策略类型 必填")
    @ApiModelProperty(value = "策略类型（0-拒绝/黑名单，1-允许/白名单）", allowableValues = "0,1", required = true)
    private Integer policyType;

    @NotBlank(message = "API接口ID 必填")
    @ApiModelProperty(value = "ip地址（多个用隔开;最多10个）", required = true)
    private String ipAddress;
}