package com.smart4y.cloud.base.access.interfaces.dtos.operation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/31 10:44
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ModifyOperationOpenCommand", description = "元素:公开访问:修改")
public class ModifyOperationOpenCommand implements Serializable {

    @NotBlank(message = "公开访问状态 必填")
    @ApiModelProperty(value = "公开访问状态", required = true)
    private Boolean operationOpen;
}