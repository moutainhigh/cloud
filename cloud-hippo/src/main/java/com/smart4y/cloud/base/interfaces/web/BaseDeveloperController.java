package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseDeveloperService;
import com.smart4y.cloud.base.domain.model.BaseDeveloper;
import com.smart4y.cloud.base.interfaces.command.AddDeveloperUserCommand;
import com.smart4y.cloud.base.interfaces.converter.BaseDeveloperConverter;
import com.smart4y.cloud.base.interfaces.query.BaseDeveloperQuery;
import com.smart4y.cloud.base.interfaces.vo.BaseDeveloperVO;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.domain.page.Page;
import com.smart4y.cloud.core.interfaces.UserAccountVO;
import com.smart4y.cloud.base.interfaces.command.AddDeveloperThirdPartyCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统用户信息
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Api(tags = "系统用户管理")
@RestController
public class BaseDeveloperController {

    @Autowired
    private BaseDeveloperConverter baseDeveloperConverter;
    @Autowired
    private BaseDeveloperService baseDeveloperService;

    /**
     * 获取登录账号信息
     */
    @ApiOperation(value = "获取账号登录信息", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "path"),
    })
    @GetMapping("/developer/login")
    public ResultMessage<UserAccountVO> developerLogin(@RequestParam(value = "username") String username) {
        UserAccountVO account = baseDeveloperService.login(username);
        return ResultMessage.ok(account);
    }

    /**
     * 系统分页用户列表
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @GetMapping("/developer")
    public ResultMessage<Page<BaseDeveloperVO>> getUserList(BaseDeveloperQuery query) {
        PageInfo<BaseDeveloper> pageInfo = baseDeveloperService.findListPage(query);
        Page<BaseDeveloperVO> result = baseDeveloperConverter.convertPage(pageInfo);
        return ResultMessage.ok(result);
    }

    /**
     * 获取所有用户列表
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @GetMapping("/developer/all")
    public ResultMessage<List<BaseDeveloperVO>> getUserAllList() {
        List<BaseDeveloper> list = baseDeveloperService.findAllList();
        List<BaseDeveloperVO> result = baseDeveloperConverter.convertList(list);
        return ResultMessage.ok(result);
    }

    /**
     * 添加系统用户
     */
    @ApiOperation(value = "添加系统用户", notes = "添加系统用户")
    @PostMapping("/developer/add")
    public ResultMessage<Long> addUser(
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
        return ResultMessage.ok(userId);
    }

    /**
     * 更新系统用户
     */
    @ApiOperation(value = "更新系统用户", notes = "更新系统用户")
    @PostMapping("/developer/update")
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
        return ResultMessage.ok();
    }


    /**
     * 修改用户密码
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @PostMapping("/developer/update/password")
    public ResultMessage updatePassword(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "password") String password
    ) {
        baseDeveloperService.updatePassword(userId, password);
        return ResultMessage.ok();
    }

    /**
     * 注册第三方系统登录账号
     */
    @ApiOperation(value = "注册第三方系统登录账号", notes = "仅限系统内部调用")
    @PostMapping("/developer/add/thirdParty")
    public ResultMessage<Void> addDeveloperThirdParty(@RequestBody AddDeveloperThirdPartyCommand command) {
        baseDeveloperService.addUserThirdParty(command);
        return ResultMessage.ok();
    }
}