package com.smart4y.cloud.base.access.interfaces.dtos.role;

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
@ApiModel(value = "ModifyRoleCommand", description = "角色:修改")
public class ModifyRoleCommand implements Serializable {

    @NotBlank(message = "角色名 必填")
    @ApiModelProperty(value = "角色名", required = true)
    private String roleName;

    @NotBlank(message = "角色编码 必填")
    @ApiModelProperty(value = "角色编码", required = true)
    private String roleCode;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;
}