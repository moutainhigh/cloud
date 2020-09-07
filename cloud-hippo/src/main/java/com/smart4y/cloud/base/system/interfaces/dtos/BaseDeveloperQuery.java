package com.smart4y.cloud.base.system.interfaces.dtos;

import com.smart4y.cloud.core.message.page.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 系统开发者用户分页查询
 *
 * @author Youtao
 *         Created by youtao on 2019/10/14.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BaseDeveloperQuery", description = "系统开发者用户分页查询")
public class BaseDeveloperQuery extends BaseQuery {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "登陆账号")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "用户类型（super-超级管理员 normal-普通管理员）")
    private String userType;
}