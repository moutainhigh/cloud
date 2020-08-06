package com.smart4y.cloud.base.access.interfaces.dtos.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/31 10:43
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "RbacMenuQuery", description = "菜单:查询")
public class RbacMenuQuery implements Serializable {

    @NotNull(message = "菜单ID 必填")
    @ApiModelProperty(value = "菜单ID", required = true)
    private Long menuId;
}