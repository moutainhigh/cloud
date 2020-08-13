package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.entity.RbacOperation;
import com.smart4y.cloud.base.access.domain.entity.RbacPrivilege;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.ModifyMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.privilege.RbacPrivilegePageQuery;
import com.smart4y.cloud.core.message.page.Page;

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
     * 系统所有操作权限
     */
    List<RbacOperation> getPrivilegesOperations();

    /**
     * 权限列表分页
     */
    Page<RbacPrivilege> getPrivilegesPage(RbacPrivilegePageQuery query);

    /**
     * 清理无效权限
     *
     * @param serviceId           权限所属服务ID
     * @param validOperationCodes 有效的操作权限编码列表
     */
    void removeInvalidPrivilegesByOperations(String serviceId, List<String> validOperationCodes);

    /**
     * 删除无效权限
     *
     * @param menuCodes 待删除的菜单
     */
    void removePrivilegesByMenus(List<String> menuCodes);

    /**
     * 新增角色权限
     */
    void addRolePrivilegesByOperations(List<String> validOperationCodes);

    /**
     * 新增角色权限
     */
    void addRolePrivilegesByMenus(List<String> menuCodes);

    /**
     * 新增权限、操作权限
     */
    void addPrivilegeOperations(String serviceId);

    /**
     * 新增权限、菜单权限
     */
    void addPrivilegeMenus(long menuId, String menuCode);

    /**
     * 修改权限、菜单权限
     */
    void modifyPrivilegeMenus(long menuId, String menuCode, String oldMenuCode);

    /**
     * 删除权限、菜单权限
     */
    void removePrivilegesByMenus(long menuId);
}