package com.smart4y.cloud.base.interfaces.command.authority;

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
@ApiModel(value = "GrantAuthorityActionCommand", description = "分配功能按钮权限")
public class GrantAuthorityActionCommand implements Serializable {

    @NotNull(message = "功能按钮ID必填")
    @ApiModelProperty(value = "功能按钮ID", required = true)
    private Long actionId;

    @ApiModelProperty("权限ID（多个以,隔开）")
    private String authorityIds;
}