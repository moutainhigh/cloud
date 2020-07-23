package com.smart4y.cloud.base.interfaces.command;

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
@ApiModel(value = "AddActionCommand", description = "添加功能按钮")
public class AddActionCommand implements Serializable {

    @NotBlank(message = "功能按钮编码必填")
    @ApiModelProperty(value = "功能按钮编码", required = true)
    private String actionCode;

    @NotBlank(message = "功能按钮名称必填")
    @ApiModelProperty(value = "功能按钮名称", required = true)
    private String actionName;

    @NotNull(message = "菜单ID必填")
    @ApiModelProperty(value = "菜单ID", required = true)
    private Long menuId;

    @ApiModelProperty(value = "是否启用")
    private Integer status = 1;

    @ApiModelProperty("排序（优先级越小越靠前）")
    private Integer priority = 0;

    @ApiModelProperty("描述")
    private String actionDesc;
}