package com.smart4y.cloud.base.interfaces.command.role;

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
@ApiModel(value = "DeleteRoleCommand", description = "删除角色")
public class DeleteRoleCommand implements Serializable {

    @NotNull(message = "角色ID必填")
    @ApiModelProperty(value = "角色ID", required = true)
    private Long roleId;
}