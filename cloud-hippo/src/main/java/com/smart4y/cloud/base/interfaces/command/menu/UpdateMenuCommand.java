package com.smart4y.cloud.base.interfaces.command.menu;

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
@ApiModel(value = "UpdateMenuCommand", description = "更新菜单")
public class UpdateMenuCommand implements Serializable {

    @NotNull(message = "菜单ID必填")
    @ApiModelProperty(value = "菜单ID", required = true)
    private Long menuId;

    @NotBlank(message = "菜单编码必填")
    @ApiModelProperty(value = "菜单编码", required = true)
    private String menuCode;

    @NotBlank(message = "菜单名称必填")
    @ApiModelProperty(value = "菜单名称", required = true)
    private String menuName;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty(value = "请求协议", allowableValues = "/,http://,https://")
    private String scheme = "/";

    @ApiModelProperty("请求路径")
    private String path;

    @ApiModelProperty("打开方式")
    private String target = "_self";

    @ApiModelProperty("是否启用")
    private Integer status = 1;

    @ApiModelProperty("父节点ID")
    private Long parentId = 0L;

    @ApiModelProperty("排序（优先级越小越靠前）")
    private Integer priority = 0;

    @ApiModelProperty("描述")
    private String menuDesc;
}