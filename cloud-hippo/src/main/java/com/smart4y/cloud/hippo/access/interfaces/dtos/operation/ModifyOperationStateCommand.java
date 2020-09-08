package com.smart4y.cloud.hippo.access.interfaces.dtos.operation;

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
@ApiModel(value = "ModifyOperationStateCommand", description = "元素:状态:修改")
public class ModifyOperationStateCommand implements Serializable {

    @NotBlank(message = "状态 必填")
    @ApiModelProperty(value = "状态", allowableValues = "10,20,30", required = true)
    private String operationState;
}