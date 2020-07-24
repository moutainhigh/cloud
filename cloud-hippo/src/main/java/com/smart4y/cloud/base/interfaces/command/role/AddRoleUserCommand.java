package com.smart4y.cloud.base.interfaces.command.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao on 2019/9/25.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "AddRoleUserCommand", description = "添加角色成员")
public class AddRoleUserCommand implements Serializable {

    @NotNull(message = "角色ID必填")
    @ApiModelProperty(value = "角色ID", required = true)
    private Long roleId;

    @NotBlank(message = "用户ID必填")
    @ApiModelProperty(value = "用户ID", required = true)
    private String userIds;
}