package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.BaseUserService;
import com.smart4y.cloud.base.domain.model.BaseUser;
import com.smart4y.cloud.base.interfaces.dtos.UpdateCurrentUserCommand;
import com.smart4y.cloud.core.dto.UserAccountVO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.security.OpenHelper;
import com.smart4y.cloud.core.security.OpenUserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.*;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@RestController
@Api(tags = "当前登陆用户")
public class CurrentUserController {

    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private RedisTokenStore redisTokenStore;

    /**
     * 获取登录账号信息
     */
    @ApiOperation(value = "获取账号登录信息", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "path"),
    })
    @GetMapping("/user/login")
    public ResultMessage<UserAccountVO> userLogin(@RequestParam(value = "username") String username) {
        UserAccountVO account = baseUserService.login(username);
        return ResultMessage.ok(account);
    }

    /**
     * 修改当前登录用户密码
     */
    @ApiOperation(value = "修改当前登录用户密码", notes = "修改当前登录用户密码")
    @GetMapping("/current/user/rest/password")
    public ResultMessage<Void> restPassword(@RequestParam(value = "password") String password) {
        baseUserService.updatePassword(OpenHelper.getUser().getUserId(), password);
        return ResultMessage.ok();
    }

    /**
     * 修改当前登录用户基本信息
     */
    @ApiOperation(value = "修改当前登录用户基本信息", notes = "修改当前登录用户基本信息")
    @PostMapping("/current/user/update")
    public ResultMessage<Void> updateUserInfo(@RequestBody UpdateCurrentUserCommand command) {
        OpenUserDetails openUserDetails = OpenHelper.getUser();
        BaseUser user = new BaseUser();
        user.setUserId(openUserDetails.getUserId());
        user.setNickName(command.getNickName());
        user.setUserDesc(command.getUserDesc());
        user.setAvatar(command.getAvatar());
        baseUserService.updateUser(user);
        openUserDetails.setNickName(command.getNickName());
        openUserDetails.setAvatar(command.getAvatar());
        OpenHelper.updateOpenUser(redisTokenStore, openUserDetails);
        return ResultMessage.ok();
    }
}