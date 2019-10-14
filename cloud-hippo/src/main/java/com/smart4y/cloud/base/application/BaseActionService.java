package com.smart4y.cloud.base.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.domain.model.BaseAction;
import com.smart4y.cloud.base.interfaces.valueobject.query.BaseActionQuery;

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
    PageInfo<BaseAction> findListPage(BaseActionQuery query);

    /**
     * 根据主键获取操作
     */
    BaseAction getAction(long actionId);

    /**
     * 查询菜单下所有操作
     */
    List<BaseAction> findListByMenuId(Long menuId);

    /**
     * 检查操作编码是否存在
     */
    Boolean isExist(String actionCode);

    /**
     * 添加操作资源
     */
    BaseAction addAction(BaseAction action);

    /**
     * 修改操作资源
     */
    BaseAction updateAction(BaseAction action);

    /**
     * 移除操作
     */
    void removeAction(Long actionId);

    /**
     * 移除菜单相关资源
     */
    void removeByMenuId(Long menuId);
}