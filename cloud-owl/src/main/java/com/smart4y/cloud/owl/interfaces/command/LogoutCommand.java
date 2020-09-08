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
@ApiModel(value = "LogoutCommand", description = "登出")
public class LogoutCommand implements Serializable {

    @NotBlank(message = "Token必填")
    @ApiModelProperty(value = "Token", required = true)
    private String token;
}