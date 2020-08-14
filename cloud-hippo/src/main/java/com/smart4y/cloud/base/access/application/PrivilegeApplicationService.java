package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.entity.RbacOperation;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.ModifyMenuCommand;

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
     * 同步服务的操作
     * <p>
     * 此方法为服务启动时同步操作时调用
     * </p>
     *
     * @param serviceId  所属服务ID
     * @param operations 服务所属全部菜单数据列表
     */
    void syncServiceOperation(String serviceId, Collection<RbacOperation> operations);
}