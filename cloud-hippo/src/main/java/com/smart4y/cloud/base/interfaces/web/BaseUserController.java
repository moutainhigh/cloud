package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.BaseRoleService;
import com.smart4y.cloud.base.application.BaseUserService;
import com.smart4y.cloud.base.domain.model.BaseRole;
import com.smart4y.cloud.base.domain.model.BaseUser;
import com.smart4y.cloud.base.interfaces.valueobject.command.AddAdminUserCommand;
import com.smart4y.cloud.base.interfaces.valueobject.command.RegisterAdminThirdPartyCommand;
import com.smart4y.cloud.core.interfaces.UserAccountVO;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.domain.ResultEntity;
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
import java.util.Map;
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
    private BaseUserService baseUserService;
    @Autowired
    private BaseRoleService baseRoleService;

    /**
     * 获取登录账号信息
     *
     * @param username 登录名
     * @return
     */
    @ApiOperation(value = "获取账号登录信息", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "path"),
    })
    @PostMapping("/user/login")
    public ResultEntity<UserAccountVO> userLogin(@RequestParam(value = "username") String username) {
        UserAccountVO account = baseUserService.login(username);
        return ResultEntity.ok(account);
    }

    /**
     * 系统分页用户列表
     *
     * @return
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @GetMapping("/user")
    public ResultEntity<IPage<BaseUser>> getUserList(@RequestParam(required = false) Map map) {
        return ResultEntity.ok(baseUserService.findListPage(new PageParams(map)));
    }

    /**
     * 获取所有用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @GetMapping("/user/all")
    public ResultEntity<List<BaseUser>> getUserAllList() {
        List<BaseUser> list = baseUserService.findAllList();
        return ResultEntity.ok(list);
    }

    /**
     * 添加系统用户
     *
     * @param userName
     * @param password
     * @param nickName
     * @param status
     * @param userType
     * @param email
     * @param mobile
     * @param userDesc
     * @param avatar
     * @return
     */
    @ApiOperation(value = "添加系统用户", notes = "添加系统用户")
    @PostMapping("/user/add")
    public ResultEntity<Long> addUser(
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
        return ResultEntity.ok(userId);
    }

    /**
     * 更新系统用户
     *
     * @param userId
     * @param nickName
     * @param status
     * @param userType
     * @param email
     * @param mobile
     * @param userDesc
     * @param avatar
     * @return
     */
    @ApiOperation(value = "更新系统用户", notes = "更新系统用户")
    @PostMapping("/user/update")
    public ResultEntity updateUser(
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
        return ResultEntity.ok();
    }


    /**
     * 修改用户密码
     *
     * @param userId
     * @param password
     * @return
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @PostMapping("/user/update/password")
    public ResultEntity updatePassword(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "password") String password
    ) {
        baseUserService.updatePassword(userId, password);
        return ResultEntity.ok();
    }

    /**
     * 用户分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @ApiOperation(value = "用户分配角色", notes = "用户分配角色")
    @PostMapping("/user/roles/add")
    public ResultEntity addUserRoles(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "roleIds", required = false) String roleIds) {
        List<Long> collect = Arrays.stream(roleIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseRoleService.saveUserRoles(userId, collect);
        return ResultEntity.ok();
    }

    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取用户已分配角色", notes = "获取用户已分配角色")
    @GetMapping("/user/roles")
    public ResultEntity<List<BaseRole>> getUserRoles(
            @RequestParam(value = "userId") Long userId) {
        return ResultEntity.ok(baseRoleService.getUserRoles(userId));
    }


    /**
     * 注册第三方系统登录账号
     *
     * @param account
     * @param password
     * @param accountType
     * @return
     */
    @ApiOperation(value = "注册第三方系统登录账号", notes = "仅限系统内部调用")
    @PostMapping("/user/add/thirdParty")
    public ResultEntity addUserThirdParty(
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
        return ResultEntity.ok();
    }
}