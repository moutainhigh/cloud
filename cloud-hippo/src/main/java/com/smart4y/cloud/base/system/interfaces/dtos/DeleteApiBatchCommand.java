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
@ApiModel(value = "DeleteApiBatchCommand", description = "批量删除接口按钮")
public class DeleteApiBatchCommand implements Serializable {

    @NotNull(message = "接口ID必填")
    @ApiModelProperty(value = "接口ID", required = true)
    private String ids;
}