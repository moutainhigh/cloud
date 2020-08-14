package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.ModifyMenuCommand;

import java.util.Collection;
import java.util.List;

/**
 * @author Youtao on 2020/8/10 12:47
 */
public interface PrivilegeApplicationService {

    /**
     * 添加菜单
     *
     * @param command 菜单信息
     */
    void createMenu(CreateMenuCommand command);

    /**
     * 更新菜单
     *
     * @param menuId  菜单ID
     * @param command 菜单信息
     */
    void modifyMenu(long menuId, ModifyMenuCommand command);

    /**
     * 移除菜单
     *
     * @param menuId 菜单ID
     */
    void removeMenu(long menuId);

    /**
     * 添加操作
     *
     * @param serviceId      所属服务ID
     * @param operationCodes 菜单标识列表
     */
    void createOperation(String serviceId, Collection<String> operationCodes);

    /**
     * 新增权限、操作权限
     */
    void addPrivilegeOperations(String serviceId);

    /**
     * 移除操作
     *
     * @param operationIds 操作ID列表
     */
    void removeOperation(Collection<Long> operationIds);


    /**
     * 新增角色权限
     */
    void addRolePrivilegesByOperations(List<String> validOperationCodes);
}