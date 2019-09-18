package com.smart4y.cloud.base.application;

import com.smart4y.cloud.base.domain.model.BaseAccount;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/18.
 */
public interface AccountService {

    /**
     * 查询 账号信息
     *
     * @param account     登录账号
     * @param accountType 账号类型
     * @param domain      所属域
     * @return 账号信息
     */
    BaseAccount getAccount(String account, String accountType, String domain);

    /**
     * 添加登录成功日志
     */
    void loginLog(BaseAccount account);
}