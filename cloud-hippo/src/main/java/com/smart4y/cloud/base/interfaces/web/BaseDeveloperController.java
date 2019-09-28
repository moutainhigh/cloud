package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.BaseDeveloperService;
import com.smart4y.cloud.base.domain.model.BaseDeveloper;
import com.smart4y.cloud.base.interfaces.valueobject.command.AddDeveloperUserCommand;
import com.smart4y.cloud.base.interfaces.valueobject.command.RegisterDeveloperThirdPartyCommand;
import com.smart4y.cloud.core.application.dto.UserAccount;
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

import java.util.List;
import java.util.Map;

/**
 * 系统用户信息
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Api(tags = "系统用户管理")
@RestController
public class BaseDeveloperController {

    @Autowired
    private BaseDeveloperService baseDeveloperService;

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
    @PostMapping("/developer/login")
    public ResultEntity<UserAccount> developerLogin(@RequestParam(value = "username") String username) {
        UserAccount account = baseDeveloperService.login(username);
        return ResultEntity.ok(account);
    }

    /**
     * 系统分页用户列表
     *
     * @return
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @GetMapping("/developer")
    public ResultEntity<IPage<BaseDeveloper>> getUserList(@RequestParam(required = false) Map map) {
        return ResultEntity.ok(baseDeveloperService.findListPage(new PageParams(map)));
    }

    /**
     * 获取所有用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @GetMapping("/developer/all")
    public ResultEntity<List<BaseDeveloper>> getUserAllList() {
        List<BaseDeveloper> list = baseDeveloperService.findAllList();
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
    @PostMapping("/developer/add")
    public ResultEntity<Long> addUser(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "userType") String userType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar
    ) {
        AddDeveloperUserCommand developer = new AddDeveloperUserCommand();
        developer.setUserName(userName);
        developer.setPassword(password);
        developer.setNickName(nickName);
        developer.setUserType(userType);
        developer.setEmail(email);
        developer.setMobile(mobile);
        developer.setUserDesc(userDesc);
        developer.setAvatar(avatar);
        developer.setStatus(status);
        long userId = baseDeveloperService.addUser(developer);
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
    @PostMapping("/developer/update")
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
        BaseDeveloper developer = new BaseDeveloper();
        developer.setUserId(userId);
        developer.setNickName(nickName);
        developer.setUserType(userType);
        developer.setEmail(email);
        developer.setMobile(mobile);
        developer.setUserDesc(userDesc);
        developer.setAvatar(avatar);
        developer.setStatus(status);
        baseDeveloperService.updateUser(developer);
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
    @PostMapping("/developer/update/password")
    public ResultEntity updatePassword(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "password") String password
    ) {
        baseDeveloperService.updatePassword(userId, password);
        return ResultEntity.ok();
    }

    /**
     * 注册第三方系统登录账号
     */
    @ApiOperation(value = "注册第三方系统登录账号", notes = "仅限系统内部调用")
    @PostMapping("/developer/add/thirdParty")
    public ResultEntity addDeveloperThirdParty(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "accountType") String accountType,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "avatar") String avatar) {
        RegisterDeveloperThirdPartyCommand command = new RegisterDeveloperThirdPartyCommand();
        command.setNickName(nickName);
        command.setUserName(account);
        command.setPassword(password);
        command.setAvatar(avatar);
        baseDeveloperService.addUserThirdParty(command, accountType);
        return ResultEntity.ok();
    }
}