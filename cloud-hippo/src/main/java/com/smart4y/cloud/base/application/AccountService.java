package com.smart4y.cloud.base.application;

import com.smart4y.cloud.base.domain.model.BaseUser;
import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.application.dto.UserAccount;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/18.
 */
public interface AccountService {

    /**
     * 登录
     */
    UserAccount login(String username);

    /**
     * 更新密码
     *
     * @param userId   用户ID
     * @param password 密码
     */
    void modifyPassword(long userId, String password);

    /**
     * 修改用户基本信息
     */
    void modifyUser(BaseUser user);

    /**
     * 获取 用户菜单权限
     *
     * @param isAdmin 是否超级管理员
     */
    List<AuthorityMenuDTO> findAuthorityMenuByUser(long userId, boolean isAdmin);
}