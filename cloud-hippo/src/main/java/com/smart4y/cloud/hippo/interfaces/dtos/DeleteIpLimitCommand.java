package com.smart4y.cloud.hippo.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/23 10:04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "DeleteIpLimitCommand", description = "删除IP策略")
public class DeleteIpLimitCommand implements Serializable {

    @NotNull(message = "策略ID 必填")
    @ApiModelProperty(value = "策略ID", required = true)
    private Long policyId;
}