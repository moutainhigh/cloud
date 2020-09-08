package com.smart4y.cloud.hippo.system.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.hippo.system.domain.model.BaseUser;
import com.smart4y.cloud.hippo.system.interfaces.dtos.AddUserCommand;
import com.smart4y.cloud.hippo.system.interfaces.dtos.AddUserThirdPartyCommand;
import com.smart4y.cloud.hippo.system.interfaces.dtos.BaseUserQuery;
import com.smart4y.cloud.core.dto.UserAccountVO;

import java.util.List;

/**
 * 系统用户资料管理
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
public interface BaseUserService {

    /**
     * 添加用户信息
     */
    long addUser(AddUserCommand command);

    /**
     * 更新系统用户
     */
    void updateUser(BaseUser baseUser);

    /**
     * 添加第三方登录用户
     */
    void addUserThirdParty(AddUserThirdPartyCommand command);

    /**
     * 更新密码
     */
    void updatePassword(long userId, String password);

    /**
     * 分页查询
     */
    PageInfo<BaseUser> findListPage(BaseUserQuery query);

    /**
     * 查询列表
     */
    List<BaseUser> findAllList();


    /**
     * 依据系统用户Id查询系统用户信息
     */
    BaseUser getUserById(long userId);

    /**
     * 获取用户权限
     */
    UserAccountVO getUserAccount(long userId);

    /**
     * 依据登录名查询系统用户信息
     */
    BaseUser getUserByUsername(String username);


    /**
     * 支持密码、手机号、email登陆
     * 其他方式没有规则，无法自动识别。需要单独开发
     *
     * @param account 登陆账号
     */
    UserAccountVO login(String account);
}