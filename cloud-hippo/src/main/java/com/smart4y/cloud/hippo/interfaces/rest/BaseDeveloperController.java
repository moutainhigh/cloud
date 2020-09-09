package com.smart4y.cloud.hippo.interfaces.rest;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.hippo.application.BaseDeveloperService;
import com.smart4y.cloud.hippo.domain.model.BaseDeveloper;
import com.smart4y.cloud.hippo.interfaces.dtos.AddDeveloperThirdPartyCommand;
import com.smart4y.cloud.hippo.interfaces.dtos.AddDeveloperUserCommand;
import com.smart4y.cloud.hippo.interfaces.dtos.UpdateDeveloperUserCommand;
import com.smart4y.cloud.hippo.interfaces.dtos.UpdatePasswordCommand;
import com.smart4y.cloud.hippo.interfaces.dtos.BaseDeveloperConverter;
import com.smart4y.cloud.hippo.interfaces.dtos.BaseDeveloperQuery;
import com.smart4y.cloud.hippo.interfaces.dtos.BaseDeveloperVO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.dto.UserAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 开发者用户信息
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Api(tags = "开发者用户管理")
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
     * 添加开发者用户
     */
    @ApiOperation(value = "添加开发者用户", notes = "添加开发者用户")
    @PostMapping("/developer/add")
    public ResultMessage<Long> addUser(@RequestBody AddDeveloperUserCommand command) {
        long userId = baseDeveloperService.addUser(command);
        return ResultMessage.ok(userId);
    }

    /**
     * 更新开发者用户
     */
    @ApiOperation(value = "更新开发者用户", notes = "更新开发者用户")
    @PostMapping("/developer/update")
    public ResultMessage<Void> updateUser(@RequestBody UpdateDeveloperUserCommand command) {
        BaseDeveloper developer = new BaseDeveloper();
        developer.setUserId(command.getUserId());
        developer.setNickName(command.getNickName());
        developer.setUserType(command.getUserType());
        developer.setEmail(command.getEmail());
        developer.setMobile(command.getMobile());
        developer.setUserDesc(command.getUserDesc());
        developer.setAvatar(command.getAvatar());
        developer.setStatus(command.getStatus());
        baseDeveloperService.updateUser(developer);
        return ResultMessage.ok();
    }

    /**
     * 修改用户密码
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @PostMapping("/developer/update/password")
    public ResultMessage<Void> updatePassword(@RequestBody UpdatePasswordCommand command) {
        baseDeveloperService.updatePassword(command.getUserId(), command.getPassword());
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