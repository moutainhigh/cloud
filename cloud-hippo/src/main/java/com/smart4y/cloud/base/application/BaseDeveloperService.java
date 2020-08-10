package com.smart4y.cloud.base.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.domain.model.BaseDeveloper;
import com.smart4y.cloud.base.interfaces.command.user.developer.AddDeveloperThirdPartyCommand;
import com.smart4y.cloud.base.interfaces.command.user.developer.AddDeveloperUserCommand;
import com.smart4y.cloud.base.interfaces.query.BaseDeveloperQuery;
import com.smart4y.cloud.core.dto.UserAccountVO;

import java.util.List;

/**
 * 系统用户资料管理
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
public interface BaseDeveloperService {

    /**
     * 添加用户信息
     */
    long addUser(AddDeveloperUserCommand command);

    /**
     * 更新系统用户
     */
    void updateUser(BaseDeveloper baseDeveloper);

    /**
     * 添加第三方登录用户
     */
    void addUserThirdParty(AddDeveloperThirdPartyCommand command);

    /**
     * 更新密码
     */
    void updatePassword(Long userId, String password);

    /**
     * 分页查询
     */
    PageInfo<BaseDeveloper> findListPage(BaseDeveloperQuery query);

    /**
     * 查询列表
     */
    List<BaseDeveloper> findAllList();

    /**
     * 依据系统用户Id查询系统用户信息
     */
    BaseDeveloper getUserById(long userId);

    /**
     * 依据登录名查询系统用户信息
     */
    BaseDeveloper getUserByUsername(String username);

    /**
     * 支持密码、手机号、email登陆
     * 其他方式没有规则，无法自动识别。需要单独开发
     *
     * @param account 登陆账号
     */
    UserAccountVO login(String account);
}