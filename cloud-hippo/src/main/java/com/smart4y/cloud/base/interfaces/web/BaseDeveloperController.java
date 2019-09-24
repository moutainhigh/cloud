package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.AccountService;
import com.smart4y.cloud.core.application.dto.UserAccount;
import com.smart4y.cloud.core.domain.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 系统用户信息
 */
@RestController
@Api(tags = "系统用户管理")
public class BaseDeveloperController {

    private final AccountService accountService;

    @Autowired
    public BaseDeveloperController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 获取登录账号信息
     *
     * @param username 登录名
     */
    @ApiOperation(value = "获取账号登录信息", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "path"),
    })
    @PostMapping("/developer/login")
    public ResultBody<UserAccount> developerLogin(@RequestParam(value = "username") String username) {
        UserAccount account = accountService.login(username);
        return ResultBody.ok().data(account);
    }
}