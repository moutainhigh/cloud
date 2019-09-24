package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.AccountService;
import com.smart4y.cloud.base.domain.model.BaseUser;
import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.domain.ResultBody;
import com.smart4y.cloud.core.infrastructure.constants.CommonConstants;
import com.smart4y.cloud.core.infrastructure.security.OpenHelper;
import com.smart4y.cloud.core.infrastructure.security.OpenUserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * TODO 当前账户管理
 */
@RestController
@Api(tags = "当前登陆用户")
public class CurrentUserController {

    private final RedisTokenStore redisTokenStore;
    private final AccountService accountService;

    @Autowired
    public CurrentUserController(RedisTokenStore redisTokenStore, AccountService accountService) {
        this.redisTokenStore = redisTokenStore;
        this.accountService = accountService;
    }

    /**
     * 修改当前登录用户密码
     */
    @ApiOperation(value = "修改当前登录用户密码", notes = "修改当前登录用户密码")
    @GetMapping("/current/user/rest/password")
    public ResultBody restPassword(@RequestParam(value = "password") String password) {
        accountService.modifyPassword(Objects.requireNonNull(OpenHelper.getUser()).getUserId(), password);
        return ResultBody.ok();
    }

    /**
     * 修改当前登录用户基本信息
     */
    @ApiOperation(value = "修改当前登录用户基本信息", notes = "修改当前登录用户基本信息")
    @PostMapping("/current/user/update")
    public ResultBody updateUserInfo(
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar
    ) {
        OpenUserDetails openUserDetails = OpenHelper.getUser();

        BaseUser user = new BaseUser();
        user.setUserId(openUserDetails.getUserId());
        user.setNickName(nickName);
        user.setUserDesc(userDesc);
        user.setAvatar(avatar);
        accountService.modifyUser(user);

        openUserDetails.setNickName(nickName);
        openUserDetails.setAvatar(avatar);
        OpenHelper.updateOpenUser(redisTokenStore, openUserDetails);
        return ResultBody.ok();
    }

    /**
     * 获取登陆用户已分配权限
     */
    @ApiOperation(value = "获取当前登录用户已分配菜单权限", notes = "获取当前登录用户已分配菜单权限")
    @GetMapping("/current/user/menu")
    public ResultBody<List<AuthorityMenuDTO>> findAuthorityMenu() {
        OpenUserDetails openUserDetails = OpenHelper.getUser();
        List<AuthorityMenuDTO> result = accountService.findAuthorityMenuByUser(
                openUserDetails.getUserId(), CommonConstants.ROOT.equals(openUserDetails.getUsername()));
        return ResultBody.ok().data(result);
    }
}