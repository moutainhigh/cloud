package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.application.GroupApplicationService;
import com.smart4y.cloud.base.access.domain.model.RbacGroup;
import com.smart4y.cloud.base.access.domain.model.RbacRole;
import com.smart4y.cloud.base.access.domain.model.RbacUser;
import com.smart4y.cloud.base.access.interfaces.dtos.group.*;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/7/30 15:03
 */
@Slf4j
@Api(tags = {"访问控制 - 组织"})
@RestController
public class GroupController extends BaseAccessController {

    @Autowired
    private GroupApplicationService groupApplicationService;

    @GetMapping("/groups/page")
    @ApiOperation(value = "组织:分页")
    public ResultMessage<Page<RbacGroup>> getGroupsPage(RbacGroupPageQuery query) {
        Page<RbacGroup> result = groupApplicationService.getGroupsPage(query);
        return ok(result);
    }

    @GetMapping("/groups")
    @ApiOperation(value = "组织:查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupParentId", value = "组织父级ID", defaultValue = "0", required = true, paramType = "query", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<List<RbacGroup>> getGroups(RbacGroupQuery query) {
        List<RbacGroup> result = groupApplicationService.getGroups(query);
        return ok(result);
    }

    @PostMapping("/groups")
    @ApiOperation(value = "组织:添加")
    public ResultMessage<Void> createGroup(@RequestBody CreateGroupCommand command) {
        return ok();
    }

    @PutMapping("/groups/{groupId}")
    @ApiOperation(value = "组织:修改")
    public ResultMessage<Void> modifyGroup(@PathVariable("groupId") Long groupId, @RequestBody ModifyGroupCommand command) {
        return ok();
    }

    @DeleteMapping("/groups/{groupId}")
    @ApiOperation(value = "组织:删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> removeGroup(@PathVariable("groupId") Long groupId) {
        return ok();
    }

    @GetMapping("/groups/{groupId}")
    @ApiOperation(value = "组织:详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<RbacGroup> viewGroup(@PathVariable("groupId") Long groupId) {
        RbacGroup result = groupApplicationService.viewGroup(groupId);
        return ok(result);
    }

    @GetMapping("/groups/{groupId}/roles")
    @ApiOperation(value = "组织:角色:所有", notes = "查询组织{groupId}下的直接角色（不查询下下级数据）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<List<RbacRole>> getGroupRoles(@PathVariable("groupId") Long groupId) {
        List<RbacRole> result = groupApplicationService.getGroupRoles(groupId);
        return ok(result);
    }

    @PostMapping("/groups/{groupId}/roles")
    @ApiOperation(value = "组织:角色:分配", notes = "为组织{groupId}分配角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> grantRole(@PathVariable("groupId") Long groupId, @RequestBody GrantGroupRoleCommand command) {
        return ok();
    }

    @DeleteMapping("/groups/{groupId}/roles/{roleIds}")
    @ApiOperation(value = "组织:角色:删除", notes = "移除组织{groupId}已分配的角色{roleIds}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456"),
            @ApiImplicitParam(name = "roleIds", value = "角色ID列表", required = true, paramType = "path", example = "222367153805459456;222367153805459457")
    })
    public ResultMessage<Void> removeGroupRole(@PathVariable("groupId") Long groupId, @PathVariable("roleIds") String roleIds) {
        return ok();
    }

    @GetMapping("/groups/{groupId}/users")
    @ApiOperation(value = "组织:用户:所有", notes = "查询组织{groupId}下的直接用户（不查询下下级数据）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<List<RbacUser>> getGroupUsers(@PathVariable("groupId") Long groupId) {
        List<RbacUser> result = groupApplicationService.getGroupUsers(groupId);
        return ok(result);
    }

    @PostMapping("/groups/{groupId}/users")
    @ApiOperation(value = "组织:用户:分配", notes = "为组织{groupId}分配用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> grantUser(@PathVariable("groupId") Long groupId, @RequestBody GrantGroupUserCommand command) {
        return ok();
    }

    @DeleteMapping("/groups/{groupId}/users/{userIds}")
    @ApiOperation(value = "组织:用户:删除", notes = "移除组织{groupId}已分配的用户{userIds}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456"),
            @ApiImplicitParam(name = "userIds", value = "用户ID列表", required = true, paramType = "path", example = "222367153805459456;222367153805459457")
    })
    public ResultMessage<Void> removeGroupUser(@PathVariable("groupId") Long groupId, @PathVariable("userIds") String userIds) {
        return ok();
    }
}