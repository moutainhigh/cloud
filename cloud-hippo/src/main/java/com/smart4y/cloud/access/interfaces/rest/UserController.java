package com.smart4y.cloud.access.interfaces.rest;

import com.smart4y.cloud.access.domain.service.UserService;
import com.smart4y.cloud.access.interfaces.dtos.user.CreateUserCommand;
import com.smart4y.cloud.access.interfaces.dtos.user.GrantUserRoleCommand;
import com.smart4y.cloud.access.interfaces.dtos.user.ModifyUserCommand;
import com.smart4y.cloud.access.interfaces.dtos.user.RbacUserPageQuery;
import com.smart4y.cloud.access.domain.entity.RbacUser;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/7/30 15:03
 */
@Slf4j
@RestController
@Api(tags = {"访问控制 - 用户"})
public class UserController extends BaseAccessController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/page")
    @ApiOperation(value = "用户:分页")
    public ResultMessage<Page<RbacUser>> getUsersPage(RbacUserPageQuery query) {
        Page<RbacUser> result = userService.getPageLike(
                query.getPage(), query.getLimit(), query.getUserId(), query.getUserName()
        );
        return ok(result);
    }

    @PostMapping("/users")
    @ApiOperation(value = "用户:添加")
    public ResultMessage<Void> createUser(@RequestBody CreateUserCommand command) {
        // TODO 用户:添加
        return ok();
    }

    @PutMapping("/users/{userId}")
    @ApiOperation(value = "用户:修改")
    public ResultMessage<Void> modifyUser(@PathVariable("userId") Long userId, @RequestBody ModifyUserCommand command) {
        // TODO 用户:修改
        return ok();
    }

    @DeleteMapping("/users/{userId}")
    @ApiOperation(value = "用户:删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> removeUser(@PathVariable("userId") Long userId) {
        // TODO 用户:删除
        return ok();
    }

    @GetMapping("/users/{userId}")
    @ApiOperation(value = "用户:详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<RbacUser> viewUser(@PathVariable("userId") Long userId) {
        // TODO 用户:详情
        return ok();
    }

    @PostMapping("/users/{userId}/roles")
    @ApiOperation(value = "用户:角色:分配", notes = "为用户{userId}分配角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> grantRole(@PathVariable("userId") Long userId, @RequestBody GrantUserRoleCommand command) {
        // TODO 用户:角色:分配 - 为用户{userId}分配角色
        return ok();
    }

    @DeleteMapping("/users/{userId}/roles/{roleIds}")
    @ApiOperation(value = "用户:角色:删除", notes = "移除用户{userId}已分配的角色{roleIds}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456"),
            @ApiImplicitParam(name = "roleIds", value = "角色ID列表", required = true, paramType = "path", example = "222367153805459456;222367153805459457")
    })
    public ResultMessage<Void> removeUserRole(@PathVariable("userId") Long userId, @PathVariable("roleIds") String roleIds) {
        // TODO 用户:角色:删除 - 移除用户{userId}已分配的角色{roleIds}
        return ok();
    }
}