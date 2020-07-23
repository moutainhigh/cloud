package com.smart4y.cloud.uaa.interfaces.command;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/23 16:57
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "AddDeveloperThirdPartyCommand", description = "注册第三方系统登录账号")
public class AddDeveloperThirdPartyCommand implements Serializable {

    /**
     * 登陆账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 账户类型
     */
    private String accountType;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String avatar;
}