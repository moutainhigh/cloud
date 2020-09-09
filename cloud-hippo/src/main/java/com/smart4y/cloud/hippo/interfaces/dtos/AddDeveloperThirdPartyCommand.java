package com.smart4y.cloud.hippo.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/23 16:57
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "AddDeveloperThirdPartyCommand", description = "注册第三方系统登录账号（仅限系统内部调用）")
public class AddDeveloperThirdPartyCommand implements Serializable {

    @NotBlank(message = "登录名必填")
    @ApiModelProperty(value = "登录名", required = true)
    private String userName;

    @NotBlank(message = "登录密码必填")
    @ApiModelProperty(value = "登录密码", required = true)
    private String password;

    @NotBlank(message = "昵称必填")
    @ApiModelProperty(value = "昵称", required = true)
    private String nickName;

    @NotBlank(message = "账户类型必填")
    @ApiModelProperty(value = "账户类型", required = true)
    private String accountType;

    @ApiModelProperty(value = "头像")
    private String avatar;
}