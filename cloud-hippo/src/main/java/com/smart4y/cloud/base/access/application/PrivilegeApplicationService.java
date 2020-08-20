package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.entity.RbacOperation;
import com.smart4y.cloud.base.access.interfaces.dtos.element.CreateElementCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.element.ModifyElementCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.ModifyMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.operation.ModifyOperationAuthCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.operation.ModifyOperationOpenCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.operation.ModifyOperationStateCommand;

import java.util.Collection;

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
     * 添加元素
     *
     * @param command 元素信息
     */
    void createElement(CreateElementCommand command);

    /**
     * 更新元素
     *
     * @param elementId 元素ID
     * @param command   元素信息
     */
    void modifyElement(long elementId, ModifyElementCommand command);

    /**
     * 移除元素
     *
     * @param elementId 元素ID
     */
    void removeElement(long elementId);

    /**
     * 同步服务的操作
     * <p>
     * 此方法为服务启动时同步操作时调用
     * </p>
     *
     * @param serviceId  所属服务ID
     * @param operations 服务所属全部菜单数据列表
     */
    void syncServiceOperation(String serviceId, Collection<RbacOperation> operations);

    /**
     * 修改操作状态
     *
     * @param operationIds 操作ID列表
     * @param command      状态信息
     */
    void modifyOperationState(Collection<Long> operationIds, ModifyOperationStateCommand command);

    /**
     * 修改操作公开访问状态
     *
     * @param operationIds 操作ID列表
     * @param command      状态信息
     */
    void modifyOperationOpen(Collection<Long> operationIds, ModifyOperationOpenCommand command);

    /**
     * 修改操作身份认证状态
     *
     * @param operationIds 操作ID列表
     * @param command      状态信息
     */
    void modifyOperationAuth(Collection<Long> operationIds, ModifyOperationAuthCommand command);

    /**
     * 移除操作
     *
     * @param operationIds 操作ID列表
     */
    void removeOperation(Collection<Long> operationIds);

    /**
     * 移除角色
     *
     * @param roleIds 角色ID列表
     */
    void removeRole(Collection<Long> roleIds);
}