package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.BaseAuthorityService;
import com.smart4y.cloud.base.application.BaseUserService;
import com.smart4y.cloud.base.domain.model.BaseAuthorityAction;
import com.smart4y.cloud.base.domain.model.BaseUser;
import com.smart4y.cloud.base.interfaces.command.authority.GrantAuthorityActionCommand;
import com.smart4y.cloud.base.interfaces.command.authority.GrantAuthorityAppCommand;
import com.smart4y.cloud.base.interfaces.command.authority.GrantAuthorityRoleCommand;
import com.smart4y.cloud.base.interfaces.command.authority.GrantAuthorityUserCommand;
import com.smart4y.cloud.base.interfaces.converter.BaseAuthorityActionConverter;
import com.smart4y.cloud.base.interfaces.vo.BaseAuthorityActionVO;
import com.smart4y.cloud.core.dto.OpenAuthority;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.constant.CommonConstants;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import com.smart4y.cloud.core.dto.AuthorityApiDTO;
import com.smart4y.cloud.core.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.dto.AuthorityResourceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@RestController
@Api(tags = "系统权限管理")
public class BaseAuthorityController {

    @Autowired
    private BaseAuthorityActionConverter baseAuthorityActionConverter;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取所有访问权限列表
     */
    @ApiOperation(value = "获取所有访问权限列表", notes = "获取所有访问权限列表")
    @GetMapping("/authority/access")
    public ResultMessage<List<AuthorityResourceDTO>> findAuthorityResource() {
        List<AuthorityResourceDTO> result = baseAuthorityService.findAuthorityResource();
        return ResultMessage.ok(result);
    }

    /**
     * 获取权限列表
     */
    @ApiOperation(value = "获取接口权限列表", notes = "获取接口权限列表")
    @GetMapping("/authority/api")
    public ResultMessage<List<AuthorityApiDTO>> findAuthorityApi(@RequestParam(value = "serviceId", required = false) String serviceId) {
        List<AuthorityApiDTO> result = baseAuthorityService.findAuthorityApi(serviceId);
        return ResultMessage.ok(result);
    }

    /**
     * 获取菜单权限列表
     */
    @ApiOperation(value = "获取菜单权限列表", notes = "获取菜单权限列表")
    @GetMapping("/authority/menu")
    public ResultMessage<List<AuthorityMenuDTO>> findAuthorityMenu() {
        List<AuthorityMenuDTO> result = baseAuthorityService.findAuthorityMenu(1);
        return ResultMessage.ok(result);
    }

    /**
     * 获取功能权限列表
     */
    @ApiOperation(value = "获取功能权限列表", notes = "获取功能权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form")
    })
    @GetMapping("/authority/action")
    public ResultMessage<List<BaseAuthorityActionVO>> findAuthorityAction(@RequestParam(value = "actionId") Long actionId) {
        List<BaseAuthorityAction> list = baseAuthorityService.findAuthorityAction(actionId);
        List<BaseAuthorityActionVO> result = baseAuthorityActionConverter.convertList(list);
        return ResultMessage.ok(result);
    }

    /**
     * 获取角色已分配权限
     */
    @ApiOperation(value = "获取角色已分配权限", notes = "获取角色已分配权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "form")
    })
    @GetMapping("/authority/role")
    public ResultMessage<List<OpenAuthority>> findAuthorityRole(@RequestParam("roleId") Long roleId) {
        List<OpenAuthority> result = baseAuthorityService.findAuthorityByRole(roleId);
        return ResultMessage.ok(result);
    }

    /**
     * 获取用户已分配权限
     *
     * @param userId 用户ID
     */
    @ApiOperation(value = "获取用户已分配权限", notes = "获取用户已分配权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "form")
    })
    @GetMapping("/authority/user")
    public ResultMessage<List<OpenAuthority>> findAuthorityUser(@RequestParam(value = "userId") Long userId) {
        BaseUser user = baseUserService.getUserById(userId);
        List<OpenAuthority> result = baseAuthorityService.findAuthorityByUser(userId, CommonConstants.ROOT.equals(user.getUserName()));
        return ResultMessage.ok(result);
    }

    /**
     * 获取应用已分配接口权限
     */
    @ApiOperation(value = "获取应用已分配接口权限", notes = "获取应用已分配接口权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", required = true, paramType = "form")
    })
    @GetMapping("/authority/app")
    public ResultMessage<List<OpenAuthority>> findAuthorityApp(@RequestParam(value = "appId") String appId) {
        List<OpenAuthority> result = baseAuthorityService.findAuthorityByApp(appId);
        return ResultMessage.ok(result);
    }

    /**
     * 分配角色权限
     */
    @ApiOperation(value = "分配角色权限", notes = "分配角色权限")
    @PostMapping("/authority/role/grant")
    public ResultMessage<Void> grantAuthorityRole(@RequestBody GrantAuthorityRoleCommand command) {
        List<Long> collect = Arrays.stream(command.getAuthorityIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseAuthorityService.addAuthorityRole(command.getRoleId(), command.getExpireTime(), collect);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 分配用户权限
     */
    @ApiOperation(value = "分配用户权限", notes = "分配用户权限")
    @PostMapping("/authority/user/grant")
    public ResultMessage<Void> grantAuthorityUser(@RequestBody GrantAuthorityUserCommand command) {
        List<Long> collect = Arrays.stream(command.getAuthorityIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseAuthorityService.addAuthorityUser(command.getUserId(), command.getExpireTime(), collect);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 分配应用权限
     */
    @ApiOperation(value = "分配应用权限", notes = "分配应用权限")
    @PostMapping("/authority/app/grant")
    public ResultMessage<Void> grantAuthorityApp(@RequestBody GrantAuthorityAppCommand command) {
        List<Long> collect = Arrays.stream(command.getAuthorityIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseAuthorityService.addAuthorityApp(command.getAppId(), command.getExpireTime(), collect);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 功能按钮绑定API
     */
    @ApiOperation(value = "分配功能按钮权限", notes = "分配功能按钮权限")
    @PostMapping("/authority/action/grant")
    public ResultMessage<Void> grantAuthorityAction(@RequestBody GrantAuthorityActionCommand command) {
        List<Long> collect = Arrays.stream(command.getAuthorityIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseAuthorityService.addAuthorityAction(command.getActionId(), collect);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}