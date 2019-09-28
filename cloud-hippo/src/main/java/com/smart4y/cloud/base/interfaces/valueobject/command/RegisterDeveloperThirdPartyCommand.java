package com.smart4y.cloud.base.interfaces.valueobject.command;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 注册第三方系统登录账号命令
 *
 * @author Youtao
 *         Created by youtao on 2019/9/25.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterDeveloperThirdPartyCommand implements Serializable {

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
     * 头像
     */
    private String avatar;
}