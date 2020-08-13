package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.domain.entity.RbacRole;
import com.smart4y.cloud.base.access.domain.service.RoleService;
import com.smart4y.cloud.base.access.interfaces.dtos.role.CreateRoleCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.role.GrantPrivilegeCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.role.ModifyRoleCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.role.RbacRolePageQuery;
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
 * on 2020/7/30 15:06
 */
@Slf4j
@RestController
@Api(tags = {"访问控制 - 角色"})
public class RoleController extends BaseAccessController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    @ApiOperation(value = "角色:所有")
    public ResultMessage<List<RbacRole>> getRoles() {
        return ok();
    }

    @GetMapping("/roles/page")
    @ApiOperation(value = "角色:分页")
    public ResultMessage<Page<RbacRole>> getRolesPage(RbacRolePageQuery query) {
        Page<RbacRole> result = roleService.getPageLike(
                query.getPage(), query.getLimit(), query.getRoleName(), query.getRoleCode());
        return ok(result);
    }

    @PostMapping("/roles")
    @ApiOperation(value = "角色:添加")
    public ResultMessage<Void> createRole(@RequestBody CreateRoleCommand command) {
        return ok();
    }

    @PutMapping("/roles/{roleId}")
    @ApiOperation(value = "角色:修改")
    public ResultMessage<Void> modifyRole(@PathVariable("roleId") Long roleId, @RequestBody ModifyRoleCommand command) {
        return ok();
    }

    @DeleteMapping("/roles/{roleId}")
    @ApiOperation(value = "角色:删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> removeRole(@PathVariable("roleId") Long roleId) {
        return ok();
    }

    @GetMapping("/roles/{roleId}")
    @ApiOperation(value = "角色:详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<RbacRole> viewRole(@PathVariable("roleId") Long roleId) {
        return ok();
    }

    @PostMapping("/roles/{roleId}/privileges")
    @ApiOperation(value = "角色:权限:分配", notes = "为角色{roleId}授予权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> grantPrivilege(@PathVariable("roleId") Long roleId, @RequestBody GrantPrivilegeCommand command) {
        return ok();
    }

    @DeleteMapping("/roles/{roleId}/privileges/{privilegeIds}")
    @ApiOperation(value = "角色:权限:删除", notes = "移除角色{roleId}已授权的权限{privilegeIds}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456"),
            @ApiImplicitParam(name = "privilegeIds", value = "权限ID列表", required = true, paramType = "path", example = "222367153805459456;222367153805459457")
    })
    public ResultMessage<Void> removeRolePrivilege(@PathVariable("roleId") Long roleId, @PathVariable("privilegeIds") String privilegeIds) {
        return ok();
    }
}