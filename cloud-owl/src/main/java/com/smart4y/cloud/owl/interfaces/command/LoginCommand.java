package com.smart4y.cloud.owl.interfaces.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/23 09:45
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "登录获取Token参数", description = "登录获取Token参数")
public class LoginCommand implements Serializable {

    @NotBlank(message = "登录名必填")
    @ApiModelProperty(value = "登录名", required = true)
    private String username;

    @NotBlank(message = "登录密码必填")
    @ApiModelProperty(value = "登录密码", required = true)
    private String password;
}