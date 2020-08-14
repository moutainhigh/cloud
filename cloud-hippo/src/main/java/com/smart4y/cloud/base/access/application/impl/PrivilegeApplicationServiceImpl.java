package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.PrivilegeApplicationService;
import com.smart4y.cloud.base.access.domain.entity.*;
import com.smart4y.cloud.base.access.domain.service.*;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.ModifyMenuCommand;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.exception.OpenAlertException;
import com.smart4y.cloud.core.message.enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    private final PrivilegeOperationService privilegeOperationService;
    private final PrivilegeMenuService privilegeMenuService;
    private final MenuService menuService;
    private final RoleService roleService;

    @Autowired
    public PrivilegeApplicationServiceImpl(PrivilegeOperationService privilegeOperationService, OperationService operationService, PrivilegeService privilegeService, PrivilegeMenuService privilegeMenuService, MenuService menuService, RoleService roleService) {
        this.privilegeOperationService = privilegeOperationService;
        this.operationService = operationService;
        this.privilegeService = privilegeService;
        this.privilegeMenuService = privilegeMenuService;
        this.menuService = menuService;
        this.roleService = roleService;
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
        RbacPrivilege privilege = new RbacPrivilege()
                .setPrivilege(menuCode)
                .setPrivilegeType("m")
                .setCreatedDate(LocalDateTime.now());
        privilegeService.save(privilege);
        RbacPrivilegeMenu privilegeMenu = new RbacPrivilegeMenu()
                .setPrivilegeId(privilege.getPrivilegeId())
                .setMenuId(record.getMenuId())
                .setCreatedDate(LocalDateTime.now());
        privilegeMenuService.save(privilegeMenu);

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
            List<Long> privilegeIds = this.getPrivilegesByMenuCodes(Collections.singletonList(oldMenuCode));
            roleService.removePrivileges(privilegeIds);
            privilegeService.removeByPrivilege("m", privilegeIds);

            // #3.2 添加菜单权限
            RbacPrivilege privilege = new RbacPrivilege()
                    .setPrivilege(newMenuCode)
                    .setPrivilegeType("m")
                    .setCreatedDate(LocalDateTime.now());
            privilegeService.save(privilege);
            RbacPrivilegeMenu privilegeMenu = new RbacPrivilegeMenu()
                    .setPrivilegeId(privilege.getPrivilegeId())
                    .setMenuId(menuId)
                    .setCreatedDate(LocalDateTime.now());
            privilegeMenuService.save(privilegeMenu);

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
        privilegeMenuService.getPrivilege(menuId)
                .ifPresent(x -> {
                    List<Long> privilegeIds = Collections.singletonList(x.getPrivilegeId());
                    roleService.removePrivileges(privilegeIds);
                    privilegeService.removeByPrivilege("m", privilegeIds);
                });
    }

    @Override
    public void createOperation(String serviceId, Collection<String> operationCodes) {
        // TODO
    }

    @Override
    public void removeOperation(Collection<Long> operationIds) {
        if (CollectionUtils.isEmpty(operationIds)) {
            return;
        }
        // 获取权限
        List<RbacPrivilegeOperation> privilegeOperations = privilegeService.getPrivilegeOperationsByOperationIds(operationIds);
        List<Long> privilegeIds = privilegeOperations.stream().map(RbacPrivilegeOperation::getPrivilegeId).collect(Collectors.toList());

        // 清除角色权限，权限操作，权限，操作
        roleService.removePrivileges(privilegeIds);
        privilegeService.removeByPrivilege("o", privilegeIds);
        operationService.removeByIds(operationIds);
    }

    @Override
    public void addRolePrivilegesByOperations(List<String> validOperationCodes) {
        // 给角色（超级管理员）添加本次新增的权限
        List<RbacOperation> rbacOperations = operationService.getByCodes(validOperationCodes);
        List<Long> operationIds = rbacOperations.stream().map(RbacOperation::getOperationId).collect(Collectors.toList());

        List<RbacPrivilegeOperation> privilegeOperations = privilegeService.getPrivilegeOperationsByOperationIds(operationIds);
        List<Long> privilegeIds = privilegeOperations.stream().map(RbacPrivilegeOperation::getPrivilegeId).collect(Collectors.toList());

        // 超级管理员角色对应权限
        List<RbacRolePrivilege> rolePrivileges = roleService.getRolePrivilegesByRoleId(ADMIN_ROLE_ID);
        List<Long> rolePrivilegeIds = rolePrivileges.stream().map(RbacRolePrivilege::getPrivilegeId).collect(Collectors.toList());
        // 新权限
        List<Long> newPrivilegeIds = privilegeIds.stream()
                .filter(privilegeId -> !rolePrivilegeIds.contains(privilegeId))
                .collect(Collectors.toList());
        roleService.grantPrivileges(ADMIN_ROLE_ID, newPrivilegeIds);
    }

    /**
     * 获取菜单标识对应的权限
     */
    private List<Long> getPrivilegesByMenuCodes(List<String> menuCodes) {
        return privilegeService.getByType("m", menuCodes).stream()
                .map(RbacPrivilege::getPrivilegeId)
                .collect(Collectors.toList());
    }

    @Override
    public void addPrivilegeOperations(String serviceId) {
        List<RbacOperation> operations = operationService.getByServiceId(serviceId);
        List<String> operationCodes = operations.stream().map(RbacOperation::getOperationCode).collect(Collectors.toList());
        Map<String, Long> operationIdCodeMap = operations.stream()
                .collect(Collectors.toMap(RbacOperation::getOperationCode, RbacOperation::getOperationId));

        List<RbacPrivilege> rbacPrivileges = privilegeService.getByType("o", operationCodes);
        List<String> privilegeCodes = rbacPrivileges.stream().map(RbacPrivilege::getPrivilege).collect(Collectors.toList());
        List<String> newCodes = operationCodes.stream()
                .filter(code -> !privilegeCodes.contains(code))
                .collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();
        List<RbacPrivilege> newPrivileges = newCodes.stream()
                .map(code -> new RbacPrivilege()
                        .setPrivilege(code)
                        .setPrivilegeType("o")
                        .setCreatedDate(now))
                .collect(Collectors.toList());
        privilegeService.saveBatch(newPrivileges);

        List<RbacPrivilegeOperation> newPrivilegeOperations = newPrivileges.stream()
                .map(privilege -> new RbacPrivilegeOperation()
                        .setPrivilegeId(privilege.getPrivilegeId())
                        .setOperationId(operationIdCodeMap.get(privilege.getPrivilege()))
                        .setCreatedDate(now))
                .collect(Collectors.toList());
        privilegeOperationService.saveBatch(newPrivilegeOperations);
    }
}