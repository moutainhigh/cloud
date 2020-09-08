package com.smart4y.cloud.hippo.access.interfaces.dtos.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/31 10:44
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ModifyMenuCommand", description = "菜单:修改")
public class ModifyMenuCommand implements Serializable {

    @NotNull(message = "父级ID 必填")
    @ApiModelProperty(value = "父级ID", required = true)
    private Long menuParentId;

    @NotBlank(message = "菜单名称 必填")
    @ApiModelProperty(value = "菜单名称", required = true)
    private String menuName;

    @NotBlank(message = "菜单标识 必填")
    @ApiModelProperty(value = "菜单标识", required = true)
    private String menuCode;

    @NotBlank(message = "菜单图标 必填")
    @ApiModelProperty(value = "菜单图标", required = true)
    private String menuIcon;

    @ApiModelProperty(value = "菜单路径")
    private String menuPath;

    @NotBlank(message = "菜单打开方式 必填")
    @ApiModelProperty(value = "菜单打开方式（/-路由，http://-HTTP，https://-HTTPs）", allowableValues = "/,http://,https://", required = true)
    private String menuSchema;

    @NotBlank(message = "菜单窗口目标 必填")
    @ApiModelProperty(value = "菜单窗口目标（_self-当前标签，_blank-新标签）", allowableValues = "_self,_blank", required = true)
    private String menuTarget;

    @NotBlank(message = "菜单状态 必填")
    @ApiModelProperty(value = "菜单状态（10-启用，20-禁用，30-锁定）", allowableValues = "10,20,30", required = true)
    private String menuState;

    @NotNull(message = "菜单排序 必填")
    @ApiModelProperty(value = "菜单排序", required = true)
    private Integer menuSorted;
}