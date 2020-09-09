package com.smart4y.cloud.hippo.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统用户-开发者信息
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "BaseDeveloperVO", description = "系统用户（开发者信息）")
public class BaseDeveloperVO implements Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 登陆账号
     */
    @ApiModelProperty(value = "登录账号")
    private String userName;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 开发者类型: isp-服务提供商 normal-自研开发者
     */
    @ApiModelProperty(value = "开发者类型（isp-服务提供商 normal-自研开发者）")
    private String userType;

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long companyId;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String userDesc;

    /**
     * 状态:0-禁用 1-正常 2-锁定
     */
    @ApiModelProperty(value = "状态（0-禁用 1-正常 2-锁定）")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间（格式：yyyy-MM-dd HH:mm:ss）")
    private String createdDate;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间（格式：yyyy-MM-dd HH:mm:ss）")
    private String lastModifiedDate;
}