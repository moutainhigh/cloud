package com.smart4y.cloud.base.interfaces.command.profile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/23 10:04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "UpdateCurrentUserCommand", description = "修改当前登录用户基本信息")
public class UpdateCurrentUserCommand implements Serializable {

    @NotBlank(message = "昵称必填")
    @ApiModelProperty(value = "昵称", required = true)
    private String nickName;

    @ApiModelProperty(value = "描述")
    private String userDesc;

    @ApiModelProperty("头像")
    private String avatar;
}