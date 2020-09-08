package com.smart4y.cloud.hippo.gateway.interfaces.dtos;

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
@ApiModel(value = "AddRateLimitApiCommand", description = "限流策略绑定API")
public class AddRateLimitApiCommand implements Serializable {

    @NotNull(message = "策略ID 必填")
    @ApiModelProperty(value = "策略ID", required = true)
    private Long policyId;

    @NotBlank(message = "API接口ID 必填")
    @ApiModelProperty(value = "API接口ID", required = true)
    private String apiIds;
}