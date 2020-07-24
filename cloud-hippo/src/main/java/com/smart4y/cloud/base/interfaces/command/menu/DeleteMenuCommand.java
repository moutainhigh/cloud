package com.smart4y.cloud.base.interfaces.command.menu;

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
@ApiModel(value = "DeleteMenuCommand", description = "删除菜单")
public class DeleteMenuCommand implements Serializable {

    @NotNull(message = "菜单ID必填")
    @ApiModelProperty(value = "菜单ID", required = true)
    private Long menuId;
}