package com.smart4y.cloud.base.system.interfaces.dtos;

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
@ApiModel(value = "UpdateApiBatchStatusCommand", description = "批量修改状态")
public class UpdateApiBatchStatusCommand implements Serializable {

    @NotNull(message = "接口ID必填")
    @ApiModelProperty(value = "接口ID", required = true)
    private String ids;

    @NotNull(message = "是否启用必填")
    @ApiModelProperty(value = "是否启用（0-禁用，1-启用）", allowableValues = "0,1", required = true)
    private Integer status;
}