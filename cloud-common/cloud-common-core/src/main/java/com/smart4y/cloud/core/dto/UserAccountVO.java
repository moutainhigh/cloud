package com.smart4y.cloud.core.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 用户账号
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "UserAccount", description = "用户账号")
public class UserAccountVO implements Serializable {

    /**
     * 账号ID
     */
    @ApiModelProperty(value = "账号ID")
    private Long accountId;

    /**
     * 系统用户Id
     */
    @ApiModelProperty(value = "系统用户Id")
    private Long userId;

    /**
     * 标识（手机号、邮箱、系统用户名、或第三方应用的唯一标识）
     */
    @ApiModelProperty(value = "标识（手机号、邮箱、系统用户名、或第三方应用的唯一标识）")
    private String account;

    /**
     * 密码凭证（站内的保存密码、站外的不保存或保存token）
     */
    @ApiModelProperty(value = "密码凭证（站内的保存密码、站外的不保存或保存token）")
    private String password;

    /**
     * 登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等
     */
    @ApiModelProperty(value = "登录类型（password-密码 mobile-手机号 email-邮箱 weixin-微信 weibo-微博 qq-QQ）")
    private String accountType;

    /**
     * 第三方账号
     */
    @ApiModelProperty(value = "第三方账号")
    private String thirdParty;

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
     * 注册IP
     */
    @ApiModelProperty(value = "注册IP")
    private String registerIp;

    /**
     * 状态:0-禁用 1-启用 2-锁定
     */
    @ApiModelProperty(value = "状态（0-禁用 1-启用 2-锁定）")
    private Integer status;

    /**
     * 账号域
     */
    @ApiModelProperty(value = "账号域")
    private String domain;

    /**
     * 角色列表
     */
    @ApiModelProperty(value = "角色列表")
    private List<Map<String, Object>> roles = Lists.newArrayList();

    /**
     * 用户权限
     */
    @ApiModelProperty(value = "用户权限")
    private List<OpenAuthority> authorities = Lists.newArrayList();
}