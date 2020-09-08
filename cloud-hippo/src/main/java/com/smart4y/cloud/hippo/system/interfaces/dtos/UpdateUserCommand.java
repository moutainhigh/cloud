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
 * 添加系统用户命令
 *
 * @author Youtao
 * Created by youtao on 2019/9/25.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "UpdateUserCommand", description = "更新系统用户")
public class UpdateUserCommand implements Serializable {

    @NotNull(message = "用户ID必填")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

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