package com.smart4y.cloud.base.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.domain.model.BaseAction;
import com.smart4y.cloud.core.domain.PageParams;

import java.util.List;

/**
 * 操作资源管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface BaseActionService {

    /**
     * 分页查询
     */
    PageInfo<BaseAction> findListPage(PageParams pageParams);

    /**
     * 根据主键获取操作
     */
    BaseAction getAction(long actionId);

    /**
     * 查询菜单下所有操作
     *
     * @param menuId
     * @return
     */
    List<BaseAction> findListByMenuId(Long menuId);

    /**
     * 检查操作编码是否存在
     *
     * @param actionCode
     * @return
     */
    Boolean isExist(String actionCode);


    /**
     * 添加操作资源
     *
     * @param action
     * @return
     */
    BaseAction addAction(BaseAction action);

    /**
     * 修改操作资源
     *
     * @param action
     * @return
     */
    BaseAction updateAction(BaseAction action);

    /**
     * 移除操作
     *
     * @param actionId
     * @return
     */
    void removeAction(Long actionId);

    /**
     * 移除菜单相关资源
     *
     * @param menuId
     */
    void removeByMenuId(Long menuId);
}
