package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.PrivilegeApplicationService;
import com.smart4y.cloud.base.access.domain.entity.*;
import com.smart4y.cloud.base.access.domain.service.MenuService;
import com.smart4y.cloud.base.access.domain.service.OperationService;
import com.smart4y.cloud.base.access.domain.service.PrivilegeService;
import com.smart4y.cloud.base.access.domain.service.RoleService;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.ModifyMenuCommand;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.exception.OpenAlertException;
import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.smart4y.cloud.base.access.domain.service.RoleService.ADMIN_ROLE_ID;

/**
 * @author Youtao on 2020/8/10 12:47
 */
@Slf4j
@ApplicationService
public class PrivilegeApplicationServiceImpl implements PrivilegeApplicationService {

    private final OperationService operationService;
    private final PrivilegeService privilegeService;
    private final MenuService menuService;
    private final RoleService roleService;
    private final OpenRestTemplate openRestTemplate;

    @Autowired
    public PrivilegeApplicationServiceImpl(OperationService operationService, PrivilegeService privilegeService, MenuService menuService, RoleService roleService, OpenRestTemplate openRestTemplate) {
        this.operationService = operationService;
        this.privilegeService = privilegeService;
        this.menuService = menuService;
        this.roleService = roleService;
        this.openRestTemplate = openRestTemplate;
    }

    @Override
    public void createMenu(CreateMenuCommand command) {
        String menuCode = command.getMenuCode();
        boolean existMenuCode = menuService.existByCode(menuCode);
        if (existMenuCode) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "菜单编码已存在：" + menuCode);
        }
        // #1 添加菜单记录
        RbacMenu record = new RbacMenu();
        BeanUtils.copyProperties(command, record);
        record.setCreatedDate(LocalDateTime.now());
        menuService.save(record);

        // #2 修改菜单父级`子节点`状态
        long menuParentId = record.getMenuParentId();
        menuService.modifyChildForExist(menuParentId);

        // #3 添加菜单权限
        privilegeService.savePrivilegeMenu(record.getMenuId(), menuCode);

        // #4 添加角色（超级管理员）对应的权限
        List<Long> privilegeIds = privilegeService
                .getByType("m", Collections.singletonList(menuCode)).stream()
                .map(RbacPrivilege::getPrivilegeId)
                .collect(Collectors.toList());
        List<RbacRolePrivilege> rolePrivileges = roleService.getRolePrivilegesByRoleId(ADMIN_ROLE_ID);
        List<Long> rolePrivilegeIds = rolePrivileges.stream()
                .map(RbacRolePrivilege::getPrivilegeId).collect(Collectors.toList());
        List<Long> newPrivilegeIds = privilegeIds.stream()
                .filter(privilegeId -> !rolePrivilegeIds.contains(privilegeId))
                .collect(Collectors.toList());
        roleService.grantPrivileges(ADMIN_ROLE_ID, newPrivilegeIds);

        openRestTemplate.refreshGateway();
    }

    @Override
    public void modifyMenu(long menuId, ModifyMenuCommand command) {
        Long menuParentId = command.getMenuParentId();
        if (menuId == menuParentId) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "父菜单不能为自身");
        }
        RbacMenu oldMenu = menuService.getById(menuId);
        String oldMenuCode = oldMenu.getMenuCode();
        long oldMenuParentId = oldMenu.getMenuParentId();
        String newMenuCode = command.getMenuCode();

        // #1 更新菜单
        RbacMenu record = new RbacMenu();
        BeanUtils.copyProperties(command, record);
        record.setMenuId(menuId);
        record.setLastModifiedDate(LocalDateTime.now());
        menuService.updateSelectiveById(record);

        // #2 修改菜单父级`子节点`状态
        if (oldMenuParentId != menuParentId) {
            boolean hasChild = menuService.hasChild(oldMenuParentId);
            if (!hasChild) {
                menuService.modifyChildForNotExist(oldMenuParentId);
            }
            menuService.modifyChildForExist(menuParentId);
        }
        // #3 修改菜单编码引起的权限变化
        if (!oldMenuCode.equals(newMenuCode)) {
            // #3.1 删除老菜单：角色权限，菜单权限，权限
            List<Long> privilegeIds = privilegeService
                    .getByType("m", Collections.singletonList(oldMenuCode)).stream()
                    .map(RbacPrivilege::getPrivilegeId)
                    .collect(Collectors.toList());
            roleService.removePrivileges(privilegeIds);
            privilegeService.removeByPrivilege("m", privilegeIds);

            // #3.2 添加菜单权限
            privilegeService.savePrivilegeMenu(menuId, newMenuCode);

            // #3.3 添加角色（超级管理员）对应的权限
            List<Long> menuPrivilegeIds = privilegeService
                    .getByType("m", Collections.singletonList(newMenuCode)).stream()
                    .map(RbacPrivilege::getPrivilegeId)
                    .collect(Collectors.toList());
            List<RbacRolePrivilege> rolePrivileges = roleService.getRolePrivilegesByRoleId(ADMIN_ROLE_ID);
            List<Long> rolePrivilegeIds = rolePrivileges.stream().map(RbacRolePrivilege::getPrivilegeId).collect(Collectors.toList());
            List<Long> newPrivilegeIds = menuPrivilegeIds.stream()
                    .filter(privilegeId -> !rolePrivilegeIds.contains(privilegeId))
                    .collect(Collectors.toList());
            roleService.grantPrivileges(ADMIN_ROLE_ID, newPrivilegeIds);
        }

        openRestTemplate.refreshGateway();
    }

    @Override
    public void removeMenu(long menuId) {
        RbacMenu rbacMenu = menuService.getById(menuId);
        if (null == rbacMenu || rbacMenu.getExistChild()) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "当前菜单存在子节点，禁止删除");
        }
        // #1 删除菜单
        menuService.removeById(menuId);

        // #2 修改菜单父级`子节点`状态
        boolean hasChild = menuService.hasChild(rbacMenu.getMenuParentId());
        if (!hasChild) {
            menuService.modifyChildForNotExist(rbacMenu.getMenuParentId());
        }

        // #3 移除角色权限、菜单权限、权限、菜单
        privilegeService
                .getPrivilegeMenuByMenuId(menuId)
                .ifPresent(x -> {
                    List<Long> privilegeIds = Collections.singletonList(x.getPrivilegeId());
                    roleService.removePrivileges(privilegeIds);
                    privilegeService.removeByPrivilege("m", privilegeIds);
                });

        openRestTemplate.refreshGateway();
    }

    @Override
    public void syncServiceOperation(String serviceId, Collection<RbacOperation> operations) {
        // 此服务的所有操作
        List<String> validOperationCodes = Objects.requireNonNull(operations).stream()
                .map(RbacOperation::getOperationCode).collect(Collectors.toList());
        // 数据库中已存在的操作
        List<RbacOperation> existOperations = operationService.getByCodes(validOperationCodes);
        Map<String, RbacOperation> existOperationMap = existOperations.stream()
                .collect(Collectors.toMap(RbacOperation::getOperationCode, Function.identity()));

        // #1 移除失效权限
        // 获取失效操作（同一个服务中，有效操作之外的示为无效操作）
        List<RbacOperation> invalidOperations = operationService
                .getOutSideByCodes(serviceId, validOperationCodes);
        List<Long> invalidOperationIds = invalidOperations.stream()
                .map(RbacOperation::getOperationId).collect(Collectors.toList());

        // 获取失效权限
        List<Long> invalidPrivilegeIds = privilegeService
                .getPrivilegeOperationsByOperationIds(invalidOperationIds).stream()
                .map(RbacPrivilegeOperation::getPrivilegeId).collect(Collectors.toList());
        // 清除角色权限，权限（含权限操作），操作
        roleService.removePrivileges(invalidPrivilegeIds);
        privilegeService.removeByPrivilege("o", invalidPrivilegeIds);
        operationService.removeByIds(invalidOperationIds);

        // #2 更新已存在的操作
        List<RbacOperation> modifyOperations = operations.stream()
                .filter(o -> existOperationMap.containsKey(o.getOperationCode()))
                .peek(o -> o.setOperationId(existOperationMap.get(o.getOperationCode()).getOperationId())
                        .setLastModifiedDate(LocalDateTime.now()))
                .collect(Collectors.toList());
        operationService.updateSelectiveBatchById(modifyOperations);

        // #3 添加新操作权限
        List<RbacOperation> newOperations = operations.stream()
                .filter(o -> !existOperationMap.containsKey(o.getOperationCode()))
                .peek(x -> x
                        .setOperationAuth(true)
                        .setOperationOpen(true)
                        .setOperationState("10")
                        .setCreatedDate(LocalDateTime.now()))
                .collect(Collectors.toList());
        operationService.saveBatch(newOperations);
        privilegeService.savePrivilegeOperation(newOperations);

        // #4 给角色（超级管理员）添加本次新增的权限
        List<Long> newOperationIds = newOperations.stream()
                .map(RbacOperation::getOperationId).collect(Collectors.toList());
        List<Long> newPrivilegeIds = privilegeService
                .getPrivilegeOperationsByOperationIds(newOperationIds).stream()
                .map(RbacPrivilegeOperation::getPrivilegeId).collect(Collectors.toList());
        roleService.grantPrivileges(ADMIN_ROLE_ID, newPrivilegeIds);

        openRestTemplate.refreshGateway();
    }
}