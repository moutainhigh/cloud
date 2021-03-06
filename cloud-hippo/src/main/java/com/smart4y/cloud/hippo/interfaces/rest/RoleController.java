package com.smart4y.cloud.hippo.interfaces.rest;

import com.smart4y.cloud.hippo.application.PrivilegeApplicationService;
import com.smart4y.cloud.hippo.domain.service.RoleService;
import com.smart4y.cloud.hippo.interfaces.dtos.role.CreateRoleCommand;
import com.smart4y.cloud.hippo.interfaces.dtos.role.GrantPrivilegeCommand;
import com.smart4y.cloud.hippo.interfaces.dtos.role.ModifyRoleCommand;
import com.smart4y.cloud.hippo.interfaces.dtos.role.RbacRolePageQuery;
import com.smart4y.cloud.hippo.domain.entity.RbacRole;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/7/30 15:06
 */
@Slf4j
@RestController
@Api(tags = {"访问控制 - 角色"})
public class RoleController extends BaseAccessController {

    private final RoleService roleService;
    private final PrivilegeApplicationService privilegeApplicationService;

    @Autowired
    public RoleController(RoleService roleService, PrivilegeApplicationService privilegeApplicationService) {
        this.roleService = roleService;
        this.privilegeApplicationService = privilegeApplicationService;
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
        RbacRole record = new RbacRole();
        BeanUtils.copyProperties(command, record);
        record.setCreatedDate(LocalDateTime.now());
        roleService.save(record);
        return ok();
    }

    @PutMapping("/roles/{roleId}")
    @ApiOperation(value = "角色:修改")
    public ResultMessage<Void> modifyRole(@PathVariable("roleId") Long roleId, @RequestBody ModifyRoleCommand command) {
        RbacRole record = new RbacRole();
        BeanUtils.copyProperties(command, record);
        record.setRoleId(roleId);
        record.setLastModifiedDate(LocalDateTime.now());
        roleService.updateSelectiveById(record);
        return ok();
    }

    @DeleteMapping("/roles/{roleId}")
    @ApiOperation(value = "角色:删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> removeRole(@PathVariable("roleId") Long roleId) {
        privilegeApplicationService.removeRole(Collections.singletonList(roleId));
        return ok();
    }


    @GetMapping("/roles/{roleId}")
    @ApiOperation(value = "角色:详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<RbacRole> viewRole(@PathVariable("roleId") Long roleId) {
        RbacRole result = roleService.getById(roleId);
        return ok(result);
    }

    @PostMapping("/roles/{roleId}/privileges")
    @ApiOperation(value = "角色:权限:分配", notes = "为角色{roleId}授予权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> grantPrivilege(@PathVariable("roleId") Long roleId, @RequestBody GrantPrivilegeCommand command) {
        // TODO 角色:权限:分配 - 为角色{roleId}授予权限
        return ok();
    }

    @DeleteMapping("/roles/{roleId}/privileges/{privilegeIds}")
    @ApiOperation(value = "角色:权限:删除", notes = "移除角色{roleId}已授权的权限{privilegeIds}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456"),
            @ApiImplicitParam(name = "privilegeIds", value = "权限ID列表", required = true, paramType = "path", example = "222367153805459456;222367153805459457")
    })
    public ResultMessage<Void> removeRolePrivilege(@PathVariable("roleId") Long roleId, @PathVariable("privilegeIds") String privilegeIds) {
        // TODO 角色:权限:删除 - 移除角色{roleId}已授权的权限{privilegeIds}
        return ok();
    }
}