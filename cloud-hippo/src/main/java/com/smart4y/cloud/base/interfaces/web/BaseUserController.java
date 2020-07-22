package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseRoleService;
import com.smart4y.cloud.base.application.BaseUserService;
import com.smart4y.cloud.base.domain.model.BaseRole;
import com.smart4y.cloud.base.domain.model.BaseUser;
import com.smart4y.cloud.base.interfaces.converter.BaseRoleConverter;
import com.smart4y.cloud.base.interfaces.converter.BaseUserConverter;
import com.smart4y.cloud.base.interfaces.valueobject.command.AddAdminUserCommand;
import com.smart4y.cloud.base.interfaces.valueobject.command.RegisterAdminThirdPartyCommand;
import com.smart4y.cloud.base.interfaces.valueobject.query.BaseUserQuery;
import com.smart4y.cloud.base.interfaces.valueobject.vo.BaseRoleVO;
import com.smart4y.cloud.base.interfaces.valueobject.vo.BaseUserVO;
import com.smart4y.cloud.core.domain.page.Page;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.interfaces.UserAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户信息
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Api(tags = "系统用户管理")
@RestController
public class BaseUserController {

    @Autowired
    private BaseUserConverter baseUserConverter;
    @Autowired
    private BaseRoleConverter baseRoleConverter;
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private BaseRoleService baseRoleService;

    /**
     * 获取登录账号信息
     */
    @ApiOperation(value = "获取账号登录信息", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "path"),
    })
    @PostMapping("/user/login")
    public ResultMessage<UserAccountVO> userLogin(@RequestParam(value = "username") String username) {
        UserAccountVO account = baseUserService.login(username);
        return ResultMessage.ok(account);
    }

    /**
     * 系统分页用户列表
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @GetMapping("/user")
    public ResultMessage<Page<BaseUserVO>> getUserList(BaseUserQuery query) {
        PageInfo<BaseUser> pageInfo = baseUserService.findListPage(query);
        Page<BaseUserVO> result = baseUserConverter.convertPage(pageInfo);
        return ResultMessage.ok(result);
    }

    /**
     * 获取所有用户列表
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @GetMapping("/user/all")
    public ResultMessage<List<BaseUserVO>> getUserAllList() {
        List<BaseUser> list = baseUserService.findAllList();
        List<BaseUserVO> result = baseUserConverter.convertList(list);
        return ResultMessage.ok(result);
    }

    /**
     * 添加系统用户
     */
    @ApiOperation(value = "添加系统用户", notes = "添加系统用户")
    @PostMapping("/user/add")
    public ResultMessage<Long> addUser(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "userType") String userType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar) {
        AddAdminUserCommand command = new AddAdminUserCommand();
        command.setUserName(userName);
        command.setPassword(password);
        command.setNickName(nickName);
        command.setUserType(userType);
        command.setEmail(email);
        command.setMobile(mobile);
        command.setUserDesc(userDesc);
        command.setAvatar(avatar);
        command.setStatus(status);
        long userId = baseUserService.addUser(command);
        return ResultMessage.ok(userId);
    }

    /**
     * 更新系统用户
     */
    @ApiOperation(value = "更新系统用户", notes = "更新系统用户")
    @PostMapping("/user/update")
    public ResultMessage updateUser(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "userType") String userType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar
    ) {
        BaseUser user = new BaseUser();
        user.setUserId(userId);
        user.setNickName(nickName);
        user.setUserType(userType);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setUserDesc(userDesc);
        user.setAvatar(avatar);
        user.setStatus(status);
        baseUserService.updateUser(user);
        return ResultMessage.ok();
    }

    /**
     * 修改用户密码
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @PostMapping("/user/update/password")
    public ResultMessage updatePassword(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "password") String password
    ) {
        baseUserService.updatePassword(userId, password);
        return ResultMessage.ok();
    }

    /**
     * 用户分配角色
     */
    @ApiOperation(value = "用户分配角色", notes = "用户分配角色")
    @PostMapping("/user/roles/add")
    public ResultMessage addUserRoles(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "roleIds", required = false) String roleIds) {
        List<Long> collect = Arrays.stream(roleIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseRoleService.saveUserRoles(userId, collect);
        return ResultMessage.ok();
    }

    /**
     * 获取用户角色
     */
    @ApiOperation(value = "获取用户已分配角色", notes = "获取用户已分配角色")
    @GetMapping("/user/roles")
    public ResultMessage<List<BaseRoleVO>> getUserRoles(
            @RequestParam(value = "userId") Long userId) {
        List<BaseRole> list = baseRoleService.getUserRoles(userId);
        List<BaseRoleVO> result = baseRoleConverter.convertList(list);
        return ResultMessage.ok(result);
    }

    /**
     * 注册第三方系统登录账号
     */
    @ApiOperation(value = "注册第三方系统登录账号", notes = "仅限系统内部调用")
    @PostMapping("/user/add/thirdParty")
    public ResultMessage addUserThirdParty(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "accountType") String accountType,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "avatar") String avatar) {
        RegisterAdminThirdPartyCommand command = new RegisterAdminThirdPartyCommand();
        command.setNickName(nickName);
        command.setUserName(account);
        command.setPassword(password);
        command.setAvatar(avatar);
        baseUserService.addUserThirdParty(command, accountType);
        return ResultMessage.ok();
    }
}