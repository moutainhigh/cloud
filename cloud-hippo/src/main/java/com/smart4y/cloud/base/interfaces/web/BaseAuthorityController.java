package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.BaseAuthorityService;
import com.smart4y.cloud.base.application.BaseUserService;
import com.smart4y.cloud.base.domain.model.BaseAuthorityAction;
import com.smart4y.cloud.base.domain.model.BaseUser;
import com.smart4y.cloud.base.interfaces.converter.BaseAuthorityActionConverter;
import com.smart4y.cloud.base.interfaces.valueobject.vo.BaseAuthorityActionVO;
import com.smart4y.cloud.core.domain.OpenAuthority;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.infrastructure.constants.CommonConstants;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
import com.smart4y.cloud.core.interfaces.AuthorityApiDTO;
import com.smart4y.cloud.core.interfaces.AuthorityMenuDTO;
import com.smart4y.cloud.core.interfaces.AuthorityResourceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
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
    public ResultMessage<List<AuthorityApiDTO>> findAuthorityApi(
            @RequestParam(value = "serviceId", required = false) String serviceId
    ) {
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
    public ResultMessage<List<BaseAuthorityActionVO>> findAuthorityAction(
            @RequestParam(value = "actionId") Long actionId) {
        List<BaseAuthorityAction> list = baseAuthorityService.findAuthorityAction(actionId);
        List<BaseAuthorityActionVO> result = baseAuthorityActionConverter.convertList(list);
        return ResultMessage.ok(result);
    }

    /**
     * 获取角色已分配权限
     */
    @ApiOperation(value = "获取角色已分配权限", notes = "获取角色已分配权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form")
    })
    @GetMapping("/authority/role")
    public ResultMessage<List<OpenAuthority>> findAuthorityRole(Long roleId) {
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
            @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "", required = true, paramType = "form")
    })
    @GetMapping("/authority/user")
    public ResultMessage<List<OpenAuthority>> findAuthorityUser(
            @RequestParam(value = "userId") Long userId
    ) {
        BaseUser user = baseUserService.getUserById(userId);
        List<OpenAuthority> result = baseAuthorityService.findAuthorityByUser(userId, CommonConstants.ROOT.equals(user.getUserName()));
        return ResultMessage.ok(result);
    }


    /**
     * 获取应用已分配接口权限
     *
     * @param appId 角色ID
     * @return
     */
    @ApiOperation(value = "获取应用已分配接口权限", notes = "获取应用已分配接口权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", defaultValue = "", required = true, paramType = "form")
    })
    @GetMapping("/authority/app")
    public ResultMessage<List<OpenAuthority>> findAuthorityApp(
            @RequestParam(value = "appId") String appId
    ) {
        List<OpenAuthority> result = baseAuthorityService.findAuthorityByApp(appId);
        return ResultMessage.ok(result);
    }

    /**
     * 分配角色权限
     *
     * @param roleId       角色ID
     * @param expireTime   授权过期时间
     * @param authorityIds 权限ID.多个以,隔开
     * @return
     */
    @ApiOperation(value = "分配角色权限", notes = "分配角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/authority/role/grant")
    public ResultMessage grantAuthorityRole(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "expireTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime expireTime,
            @RequestParam(value = "authorityIds", required = false) String authorityIds) {
        List<Long> collect = Arrays.stream(authorityIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseAuthorityService.addAuthorityRole(roleId, expireTime, collect);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }


    /**
     * 分配用户权限
     *
     * @param userId       用户ID
     * @param expireTime   授权过期时间
     * @param authorityIds 权限ID.多个以,隔开
     * @return
     */
    @ApiOperation(value = "分配用户权限", notes = "分配用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/authority/user/grant")
    public ResultMessage grantAuthorityUser(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "expireTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime expireTime,
            @RequestParam(value = "authorityIds", required = false) String authorityIds) {
        List<Long> collect = Arrays.stream(authorityIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseAuthorityService.addAuthorityUser(userId, expireTime, collect);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }


    /**
     * 分配应用权限
     *
     * @param appId        应用Id
     * @param expireTime   授权过期时间
     * @param authorityIds 权限ID.多个以,隔开
     */
    @ApiOperation(value = "分配应用权限", notes = "分配应用权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/authority/app/grant")
    public ResultMessage grantAuthorityApp(
            @RequestParam(value = "appId") String appId,
            @RequestParam(value = "expireTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime expireTime,
            @RequestParam(value = "authorityIds", required = false) String authorityIds) {
        List<Long> collect = Arrays.stream(authorityIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseAuthorityService.addAuthorityApp(appId, expireTime, collect);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 功能按钮绑定API
     */
    @ApiOperation(value = "功能按钮授权", notes = "功能按钮授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", required = false, value = "全新ID:多个用,号隔开", paramType = "form"),
    })
    @PostMapping("/authority/action/grant")
    public ResultMessage grantAuthorityAction(
            @RequestParam(value = "actionId") Long actionId,
            @RequestParam(value = "authorityIds", required = false) String authorityIds) {
        List<Long> collect = Arrays.stream(authorityIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        baseAuthorityService.addAuthorityAction(actionId, collect);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}