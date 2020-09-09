package com.smart4y.cloud.hippo.application;

import com.smart4y.cloud.hippo.domain.model.BaseAccount;
import com.smart4y.cloud.hippo.domain.model.BaseAccountLogs;

/**
 * 系统用户登录账号管理
 * 支持多账号登陆
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface BaseAccountService {

    /**
     * 根据主键获取账号信息
     *
     * @param accountId
     * @return
     */
    BaseAccount getAccountById(long accountId);

    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    BaseAccount getAccount(String account, String accountType, String domain);


    /**
     * 注册账号
     *
     * @param userId
     * @param account
     * @param password
     * @param accountType
     * @param status
     * @param domain
     * @param registerIp
     * @return
     */
    BaseAccount register(Long userId, String account, String password, String accountType, Integer status, String domain, String registerIp);


    /**
     * 检查账号是否存在
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    boolean isExist(String account, String accountType, String domain);


    /**
     * 删除账号
     *
     * @param accountId
     * @return
     */
    int removeAccount(long accountId);

    /**
     * 更新账号状态
     *
     * @param accountId
     * @param status
     */
    int updateStatus(long accountId, int status);

    /**
     * 根据用户更新账户状态
     *
     * @param userId
     * @param domain
     * @param status
     */
    int updateStatusByUserId(long userId, String domain, int status);

    /**
     * 重置用户密码
     *
     * @param userId
     * @param domain
     * @param password
     */
    int updatePasswordByUserId(Long userId, String domain, String password);

    /**
     * 根据用户ID删除账号
     *
     * @param userId
     * @param domain
     * @return
     */
    int removeAccountByUserId(Long userId, String domain);

    /**
     * 添加登录日志
     *
     * @param log
     */
    void addLoginLog(BaseAccountLogs log);
}