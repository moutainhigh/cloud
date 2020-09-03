package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.application.GroupApplicationService;
import com.smart4y.cloud.base.access.domain.entity.*;
import com.smart4y.cloud.base.access.domain.service.GroupService;
import com.smart4y.cloud.base.access.domain.service.RoleService;
import com.smart4y.cloud.base.access.domain.service.UserService;
import com.smart4y.cloud.base.access.interfaces.dtos.group.*;
import com.smart4y.cloud.core.message.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/7/30 15:03
 */
@Slf4j
@Api(tags = {"访问控制 - 组织"})
@RestController
public class GroupController extends BaseAccessController {

    private final GroupService groupService;
    private final RoleService roleService;
    private final UserService userService;
    private final GroupApplicationService groupApplicationService;

    @Autowired
    public GroupController(GroupService groupService, RoleService roleService, UserService userService, GroupApplicationService groupApplicationService) {
        this.groupService = groupService;
        this.roleService = roleService;
        this.userService = userService;
        this.groupApplicationService = groupApplicationService;
    }

    @GetMapping("/groups")
    @ApiOperation(value = "组织:查询")
    public ResultMessage<List<RbacGroup>> getGroups(@RequestParam("groupTypes") List<String> groupTypes) {
        List<RbacGroup> result = groupService.getGroupsByTypes(groupTypes);
        return ok(result);
    }

    @GetMapping("/groups/children")
    @ApiOperation(value = "组织:子级:查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", defaultValue = "0", required = true, paramType = "query", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<List<RbacGroup>> getGroupChildren(RbacGroupQuery query) {
        List<RbacGroup> result = groupService.getChildrenByParentId(query.getGroupId());
        return ok(result);
    }

    @PostMapping("/groups")
    @ApiOperation(value = "组织:添加")
    public ResultMessage<Void> createGroup(@RequestBody CreateGroupCommand command) {
        groupApplicationService.createGroup(command);
        return ok();
    }

    @PutMapping("/groups/{groupId}")
    @ApiOperation(value = "组织:修改")
    public ResultMessage<Void> modifyGroup(@PathVariable("groupId") Long groupId, @RequestBody ModifyGroupCommand command) {
        // TODO 组织:修改
        return ok();
    }

    @DeleteMapping("/groups/{groupId}")
    @ApiOperation(value = "组织:删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> removeGroup(@PathVariable("groupId") Long groupId) {
        groupApplicationService.removeGroup(groupId);
        return ok();
    }

    @GetMapping("/groups/{groupId}")
    @ApiOperation(value = "组织:详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<RbacGroup> viewGroup(@PathVariable("groupId") Long groupId) {
        RbacGroup result = groupService.getById(groupId);
        return ok(result);
    }

    @GetMapping("/groups/{groupId}/roles")
    @ApiOperation(value = "组织:角色:所有", notes = "查询组织{groupId}下的直接角色（不查询下下级数据）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<List<RbacRole>> getGroupRoles(@PathVariable("groupId") Long groupId) {
        List<Long> roleIds = groupService.getGroupRolesByGroupId(groupId).stream()
                .map(RbacGroupRole::getRoleId)
                .collect(Collectors.toList());
        List<RbacRole> result = roleService.getByIds(roleIds);
        return ok(result);
    }

    @PostMapping("/groups/{groupId}/roles")
    @ApiOperation(value = "组织:角色:分配", notes = "为组织{groupId}分配角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> grantRole(@PathVariable("groupId") Long groupId, @RequestBody GrantGroupRoleCommand command) {
        // TODO 组织:角色:分配 - 为组织{groupId}分配角色
        return ok();
    }

    @DeleteMapping("/groups/{groupId}/roles/{roleIds}")
    @ApiOperation(value = "组织:角色:删除", notes = "移除组织{groupId}已分配的角色{roleIds}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456"),
            @ApiImplicitParam(name = "roleIds", value = "角色ID列表", required = true, paramType = "path", example = "222367153805459456;222367153805459457")
    })
    public ResultMessage<Void> removeGroupRole(@PathVariable("groupId") Long groupId, @PathVariable("roleIds") String roleIds) {
        // TODO 组织:角色:删除 - 移除组织{groupId}已分配的角色{roleIds}
        return ok();
    }

    @GetMapping("/groups/{groupId}/users")
    @ApiOperation(value = "组织:用户:所有", notes = "查询组织{groupId}下的直接用户（不查询下下级数据）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<List<RbacUser>> getGroupUsers(@PathVariable("groupId") Long groupId) {
        List<Long> userIds = groupService.getGroupUsersByGroupId(groupId).stream()
                .map(RbacGroupUser::getUserId)
                .collect(Collectors.toList());
        List<RbacUser> result = userService.getByIds(userIds);
        return ok(result);
    }

    @PostMapping("/groups/{groupId}/users")
    @ApiOperation(value = "组织:用户:分配", notes = "为组织{groupId}分配用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> grantUser(@PathVariable("groupId") Long groupId, @RequestBody GrantGroupUserCommand command) {
        // TODO 组织:用户:分配 - 为组织{groupId}分配用户
        return ok();
    }

    @DeleteMapping("/groups/{groupId}/users/{userIds}")
    @ApiOperation(value = "组织:用户:删除", notes = "移除组织{groupId}已分配的用户{userIds}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456"),
            @ApiImplicitParam(name = "userIds", value = "用户ID列表", required = true, paramType = "path", example = "222367153805459456;222367153805459457")
    })
    public ResultMessage<Void> removeGroupUser(@PathVariable("groupId") Long groupId, @PathVariable("userIds") String userIds) {
        // TODO 组织:用户:删除 - 移除组织{groupId}已分配的用户{userIds}
        return ok();
    }
}