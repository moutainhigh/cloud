package com.smart4y.cloud.base.application;

import com.smart4y.cloud.base.domain.model.BaseUser;
import com.smart4y.cloud.base.interfaces.valueobject.command.AddAdminUserCommand;
import com.smart4y.cloud.base.interfaces.valueobject.command.RegisterAdminThirdPartyCommand;
import com.smart4y.cloud.core.interfaces.UserAccountVO;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.PageParams;

import java.util.List;

/**
 * 系统用户资料管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface BaseUserService {

    /**
     * 添加用户信息
     */
    long addUser(AddAdminUserCommand command);

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    void updateUser(BaseUser baseUser);

    /**
     * 添加第三方登录用户
     */
    void addUserThirdParty(RegisterAdminThirdPartyCommand command, String accountType);

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    void updatePassword(long userId, String password);

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<BaseUser> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<BaseUser> findAllList();


    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    BaseUser getUserById(long userId);

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    UserAccountVO getUserAccount(long userId);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    BaseUser getUserByUsername(String username);


    /**
     * 支持密码、手机号、email登陆
     * 其他方式没有规则，无法自动识别。需要单独开发
     *
     * @param account 登陆账号
     * @return
     */
    UserAccountVO login(String account);
}