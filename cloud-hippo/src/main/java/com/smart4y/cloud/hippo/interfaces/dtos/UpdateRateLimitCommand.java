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
@ApiModel(value = "UpdateRateLimitCommand", description = "更新限流策略")
public class UpdateRateLimitCommand implements Serializable {

    @NotNull(message = "策略ID 必填")
    @ApiModelProperty(value = "策略ID", required = true)
    private Long policyId;

    @NotBlank(message = "策略名称 必填")
    @ApiModelProperty(value = "策略名称", required = true)
    private String policyName;

    @NotBlank(message = "策略类型 必填")
    @ApiModelProperty(value = "策略类型（url,origin,user）", allowableValues = "url,origin,user", required = true)
    private String policyType;

    @NotNull(message = "限制数 必填")
    @ApiModelProperty(value = "限制数", required = true)
    private Long limitQuota;

    @NotBlank(message = "单位时间 必填")
    @ApiModelProperty(value = "单位时间（seconds,minutes,hours,days）", allowableValues = "seconds,minutes,hours,days", required = true)
    private String intervalUnit;
}