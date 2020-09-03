package com.smart4y.cloud.base.access.interfaces.dtos.element;

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
@ApiModel(value = "CreateElementCommand", description = "元素:添加")
public class CreateElementCommand implements Serializable {

    @NotBlank(message = "元素名 必填")
    @ApiModelProperty(value = "元素名", required = true)
    private String elementName;

    @NotBlank(message = "元素标识 必填")
    @ApiModelProperty(value = "元素标识", required = true)
    private String elementCode;
}