package com.smart4y.cloud.base.interfaces.valueobject.command;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 添加系统用户命令
 *
 * @author Youtao
 *         Created by youtao on 2019/9/25.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AddAdminUserCommand implements Serializable {

    /**
     * 登陆账号
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 开发者类型: isp-服务提供商 normal-自研开发者
     */
    private String userType;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 描述
     */
    private String userDesc;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 状态:0-禁用 1-正常 2-锁定
     */
    private Integer status;
}