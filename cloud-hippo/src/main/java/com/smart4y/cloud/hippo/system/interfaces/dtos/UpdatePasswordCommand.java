package com.smart4y.cloud.hippo.system.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改用户密码
 *
 * @author Youtao
 * Created by youtao on 2019/9/25.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "UpdatePasswordCommand", description = "修改用户密码")
public class UpdatePasswordCommand implements Serializable {

    @NotNull(message = "用户ID必填")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @NotBlank(message = "登录密码必填")
    @ApiModelProperty(value = "登录密码", required = true)
    private String password;
}