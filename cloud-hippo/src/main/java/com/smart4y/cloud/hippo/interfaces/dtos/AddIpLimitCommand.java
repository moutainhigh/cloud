package com.smart4y.cloud.hippo.interfaces.dtos;

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
@ApiModel(value = "AddIpLimitCommand", description = "添加IP策略")
public class AddIpLimitCommand implements Serializable {

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