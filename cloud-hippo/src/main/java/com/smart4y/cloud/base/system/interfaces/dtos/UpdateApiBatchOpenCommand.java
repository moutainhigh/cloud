package com.smart4y.cloud.base.system.interfaces.dtos;

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
@ApiModel(value = "UpdateApiBatchOpenCommand", description = "批量修改公开状态")
public class UpdateApiBatchOpenCommand implements Serializable {

    @NotBlank(message = "接口ID必填")
    @ApiModelProperty(value = "接口ID", required = true)
    private String ids;

    @NotNull(message = "是否公开必填")
    @ApiModelProperty(value = "是否公开（0-否，1-是）", allowableValues = "0,1", required = true)
    private Integer open;
}