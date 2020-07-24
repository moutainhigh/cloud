package com.smart4y.cloud.base.interfaces.command.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 添加角色
 *
 * @author Youtao
 * Created by youtao on 2019/9/25.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "AddRoleCommand", description = "添加角色")
public class AddRoleCommand implements Serializable {

    @NotBlank(message = "角色编码必填")
    @ApiModelProperty(value = "角色编码", required = true)
    private String roleCode;

    @NotBlank(message = "角色名称必填")
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    @ApiModelProperty(value = "状态（0-禁用 1-启用）", allowableValues = "0,1")
    private Integer status = 1;
}