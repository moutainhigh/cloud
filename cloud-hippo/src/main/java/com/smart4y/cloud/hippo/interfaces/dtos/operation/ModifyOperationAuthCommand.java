package com.smart4y.cloud.hippo.interfaces.dtos.operation;

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
@ApiModel(value = "ModifyOperationAuthCommand", description = "元素:身份认证:修改")
public class ModifyOperationAuthCommand implements Serializable {

    @NotBlank(message = "身份认证状态 必填")
    @ApiModelProperty(value = "身份认证状态", required = true)
    private Boolean operationAuth;
}