package com.smart4y.cloud.hippo.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分配用户角色
 *
 * @author Youtao
 * Created by youtao on 2019/9/25.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "AddUserRoleCommand", description = "分配用户角色")
public class AddUserRoleCommand implements Serializable {

    @NotNull(message = "用户ID必填")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @NotBlank(message = "角色ID必填")
    @ApiModelProperty(value = "角色ID", required = true)
    private String roleIds;
}