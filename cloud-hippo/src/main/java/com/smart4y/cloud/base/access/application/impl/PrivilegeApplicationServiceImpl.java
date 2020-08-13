package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.PrivilegeApplicationService;
import com.smart4y.cloud.base.access.domain.model.*;
import com.smart4y.cloud.base.access.domain.service.*;
import com.smart4y.cloud.base.access.interfaces.dtos.privilege.RbacPrivilegePageQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.message.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final RolePrivilegeService rolePrivilegeService;
    private final MenuService menuService;

    @Autowired
    public PrivilegeApplicationServiceImpl(PrivilegeOperationService privilegeOperationService, OperationService operationService, PrivilegeService privilegeService, RolePrivilegeService rolePrivilegeService, PrivilegeMenuService privilegeMenuService, MenuService menuService) {
        this.privilegeOperationService = privilegeOperationService;
        this.operationService = operationService;
        this.privilegeService = privilegeService;
        this.rolePrivilegeService = rolePrivilegeService;
        this.privilegeMenuService = privilegeMenuService;
        this.menuService = menuService;
    }

    @Override
    public Page<RbacPrivilege> getPrivilegesPage(RbacPrivilegePageQuery query) {
        Weekend<RbacPrivilege> weekend = Weekend.of(RbacPrivilege.class);
        WeekendCriteria<RbacPrivilege, Object> criteria = weekend.weekendCriteria();
        if (null != query.getPrivilegeId()) {
            criteria.andLike(RbacPrivilege::getPrivilegeId, "%" + query.getPrivilegeId() + "%");
        }
        if (StringUtils.isNotBlank(query.getPrivilege())) {
            criteria.andLike(RbacPrivilege::getPrivilege, "%" + query.getPrivilege() + "%");
        }
        if (StringUtils.isNotBlank(query.getPrivilegeType())) {
            criteria.andEqualTo(RbacPrivilege::getPrivilegeType, query.getPrivilegeType());
        }
        weekend
                .orderBy("createdDate").desc();

        return privilegeService.findPage(weekend, query.getPage(), query.getLimit());
    }

    @Override
    public void removeInvalidPrivilegesByOperations(String serviceId, List<String> validOperationCodes) {
        if (CollectionUtils.isEmpty(validOperationCodes)) {
            return;
        }
        // 获取失效操作列表
        List<RbacOperation> invalidOperations = operationService.getInvalidOperations(serviceId, validOperationCodes);
        List<Long> operationIds = invalidOperations.stream().map(RbacOperation::getOperationId).collect(Collectors.toList());
        // 获取权限
        List<RbacPrivilegeOperation> privilegeOperations = privilegeOperationService.getPrivileges(operationIds);
        List<Long> privilegeIds = privilegeOperations.stream().map(RbacPrivilegeOperation::getPrivilegeId).collect(Collectors.toList());

        // 清除角色权限，权限操作，权限，操作
        rolePrivilegeService.removeByPrivilege(privilegeIds);
        privilegeOperationService.removeByPrivilege(privilegeIds);
        privilegeService.removeByPrivilege(privilegeIds);
        operationService.removeByOperations(operationIds);
    }

    @Override
    public void removePrivilegesByMenus(List<String> menuCodes) {
        if (CollectionUtils.isEmpty(menuCodes)) {
            return;
        }
        // 清除角色权限，菜单权限，权限
        List<Long> privilegeIds = this.getPrivilegesByMenuCodes(menuCodes);
        rolePrivilegeService.removeByPrivilege(privilegeIds);
        privilegeMenuService.removeByPrivilege(privilegeIds);
        privilegeService.removeByPrivilege(privilegeIds);
    }

    @Override
    public void addRolePrivilegesByOperations(List<String> validOperationCodes) {
        // 给角色（超级管理员）添加本次新增的权限
        List<RbacOperation> rbacOperations = operationService.getByCodes(validOperationCodes);
        List<Long> operationIds = rbacOperations.stream().map(RbacOperation::getOperationId).collect(Collectors.toList());

        List<RbacPrivilegeOperation> privilegeOperations = privilegeOperationService.getPrivileges(operationIds);
        List<Long> privilegeIds = privilegeOperations.stream().map(RbacPrivilegeOperation::getPrivilegeId).collect(Collectors.toList());

        // 超级管理员角色对应权限
        List<RbacRolePrivilege> rolePrivileges = rolePrivilegeService.getRoles(ADMIN_ROLE_ID, privilegeIds);
        List<Long> rolePrivilegeIds = rolePrivileges.stream().map(RbacRolePrivilege::getPrivilegeId).collect(Collectors.toList());
        // 新权限
        List<Long> newPrivilegeIds = privilegeIds.stream()
                .filter(privilegeId -> !rolePrivilegeIds.contains(privilegeId))
                .collect(Collectors.toList());
        rolePrivilegeService.add(ADMIN_ROLE_ID, newPrivilegeIds);
    }

    @Override
    public void addRolePrivilegesByMenus(List<String> menuCodes) {
        // 给角色（超级管理员）添加本次新增的权限
        List<Long> privilegeIds = getPrivilegesByMenuCodes(menuCodes);

        // 超级管理员角色对应权限
        List<RbacRolePrivilege> rolePrivileges = rolePrivilegeService.getRoles(ADMIN_ROLE_ID, privilegeIds);
        List<Long> rolePrivilegeIds = rolePrivileges.stream().map(RbacRolePrivilege::getPrivilegeId).collect(Collectors.toList());
        // 新权限
        List<Long> newPrivilegeIds = privilegeIds.stream()
                .filter(privilegeId -> !rolePrivilegeIds.contains(privilegeId))
                .collect(Collectors.toList());
        rolePrivilegeService.add(ADMIN_ROLE_ID, newPrivilegeIds);
    }

    /**
     * 获取菜单标识对应的权限
     */
    private List<Long> getPrivilegesByMenuCodes(List<String> menuCodes) {
        return privilegeService.getByPrivileges("m", menuCodes).stream()
                .map(RbacPrivilege::getPrivilegeId)
                .collect(Collectors.toList());
    }

    @Override
    public void addPrivilegeOperations(String serviceId) {
        List<RbacOperation> operations = operationService.getByServiceId(serviceId);
        List<String> operationCodes = operations.stream().map(RbacOperation::getOperationCode).collect(Collectors.toList());
        Map<String, Long> operationIdCodeMap = operations.stream()
                .collect(Collectors.toMap(RbacOperation::getOperationCode, RbacOperation::getOperationId));


        List<RbacPrivilege> rbacPrivileges = privilegeService.getByPrivileges("o", operationCodes);
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

    @Override
    public void addPrivilegeMenus(long menuId, String menuCode) {
        RbacPrivilege privilege = new RbacPrivilege()
                .setPrivilege(menuCode)
                .setPrivilegeType("m")
                .setCreatedDate(LocalDateTime.now());
        privilegeService.save(privilege);

        RbacPrivilegeMenu privilegeMenu = new RbacPrivilegeMenu()
                .setPrivilegeId(privilege.getPrivilegeId())
                .setMenuId(menuId)
                .setCreatedDate(LocalDateTime.now());
        privilegeMenuService.save(privilegeMenu);

        this.addRolePrivilegesByMenus(Collections.singletonList(menuCode));
    }

    @Override
    public void modifyPrivilegeMenus(long menuId, String menuCode, String oldMenuCode) {
        // 删除老菜单权限
        this.removePrivilegesByMenus(Collections.singletonList(oldMenuCode));

        this.addPrivilegeMenus(menuId, menuCode);
    }

    @Override
    public void removePrivilegesByMenus(long menuId) {
        // 移除角色权限、菜单权限、权限、菜单
        Optional<RbacPrivilegeMenu> privilegeMenuOptional = privilegeMenuService.getPrivilege(menuId);
        privilegeMenuOptional.ifPresent(x -> {
            long privilegeId = x.getPrivilegeId();
            List<Long> privilegeIds = Collections.singletonList(privilegeId);
            rolePrivilegeService.removeByPrivilege(privilegeIds);
            privilegeMenuService.removeByPrivilege(privilegeIds);
            privilegeService.removeByPrivilege(privilegeIds);
        });
    }

    @Override
    public List<RbacOperation> getPrivilegesOperations() {
        Weekend<RbacPrivilege> privilegeWeekend = Weekend.of(RbacPrivilege.class);
        privilegeWeekend
                .weekendCriteria()
                .andEqualTo(RbacPrivilege::getPrivilegeType, "o");
        List<Long> privilegeIds = privilegeService.list(privilegeWeekend).stream()
                .map(RbacPrivilege::getPrivilegeId).collect(Collectors.toList());

        List<Long> operationIds = privilegeOperationService.getOperations(privilegeIds).stream()
                .map(RbacPrivilegeOperation::getOperationId).collect(Collectors.toList());

        return operationService.getOperations(operationIds);
    }
}