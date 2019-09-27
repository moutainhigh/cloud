package com.smart4y.cloud.core.application.dto;

import com.google.common.collect.Lists;
import com.smart4y.cloud.core.domain.OpenAuthority;
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
public class UserAccount implements Serializable {

    private List<Map<String, Object>> roles = Lists.newArrayList();
    /**
     * 用户权限
     */
    private List<OpenAuthority> authorities = Lists.newArrayList();
    /**
     * 第三方账号
     */
    private String thirdParty;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String avatar;
    private Long accountId;
    /**
     * 系统用户Id
     */
    private Long userId;
    /**
     * 标识：手机号、邮箱、 系统用户名、或第三方应用的唯一标识
     */
    private String account;
    /**
     * 密码凭证：站内的保存密码、站外的不保存或保存token）
     */
    private String password;
    /**
     * 登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等
     */
    private String accountType;
    /**
     * 注册IP
     */
    private String registerIp;
    /**
     * 状态:0-禁用 1-启用 2-锁定
     */
    private Integer status;
    /**
     * 账号域
     */
    private String domain;
}