package com.smart4y.cloud.base.application;

import com.smart4y.cloud.base.domain.model.BaseDeveloper;
import com.smart4y.cloud.base.interfaces.command.AddDeveloperUserCommand;
import com.smart4y.cloud.base.interfaces.command.RegisterDeveloperThirdPartyCommand;
import com.smart4y.cloud.core.application.dto.UserAccount;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.PageParams;

import java.util.List;

/**
 * 系统用户资料管理
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:38
 * @description:
 */
public interface BaseDeveloperService {

    /**
     * 添加用户信息
     */
    long addUser(AddDeveloperUserCommand command);

    /**
     * 更新系统用户
     *
     * @param baseDeveloper
     * @return
     */
    void updateUser(BaseDeveloper baseDeveloper);

    /**
     * 添加第三方登录用户
     */
    void addUserThirdParty(RegisterDeveloperThirdPartyCommand command, String accountType);

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    void updatePassword(Long userId, String password);

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<BaseDeveloper> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<BaseDeveloper> findAllList();


    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    BaseDeveloper getUserById(long userId);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    BaseDeveloper getUserByUsername(String username);


    /**
     * 支持密码、手机号、email登陆
     * 其他方式没有规则，无法自动识别。需要单独开发
     *
     * @param account 登陆账号
     * @return
     */
    UserAccount login(String account);
}
