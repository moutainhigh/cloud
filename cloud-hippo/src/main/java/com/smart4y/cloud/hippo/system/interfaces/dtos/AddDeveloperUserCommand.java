package com.smart4y.cloud.hippo.system.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 添加开发者用户命令
 *
 * @author Youtao
 * Created by youtao on 2019/9/25.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "AddDeveloperUserCommand", description = "添加开发者用户")
public class AddDeveloperUserCommand implements Serializable {

    @NotBlank(message = "登录名必填")
    @ApiModelProperty(value = "登录名", required = true)
    private String userName;

    @NotBlank(message = "登录密码必填")
    @ApiModelProperty(value = "登录密码", required = true)
    private String password;

    @NotBlank(message = "昵称必填")
    @ApiModelProperty(value = "昵称", required = true)
    private String nickName;

    @NotBlank(message = "开发者类型必填")
    @ApiModelProperty(value = "开发者类型（isp-服务提供商 normal-自研开发者）", allowableValues = "isp,normal", required = true)
    private String userType;


    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "描述")
    private String userDesc;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "状态（0-禁用 1-正常 2-锁定）", allowableValues = "0,1,2")
    private Integer status = 1;
}