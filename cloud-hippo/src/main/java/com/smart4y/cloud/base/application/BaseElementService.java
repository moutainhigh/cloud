package com.smart4y.cloud.base.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.domain.model.BaseElement;
import com.smart4y.cloud.base.interfaces.query.BaseActionQuery;

import java.util.List;

/**
 * 操作资源管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface BaseElementService {

    /**
     * 分页查询
     */
    PageInfo<BaseElement> findListPage(BaseActionQuery query);

    /**
     * 根据主键获取操作
     */
    BaseElement getAction(long actionId);

    /**
     * 查询菜单下所有操作
     */
    List<BaseElement> findListByMenuId(Long menuId);

    /**
     * 检查操作编码是否存在
     */
    Boolean isExist(String actionCode);

    /**
     * 添加操作资源
     */
    BaseElement addAction(BaseElement action);

    /**
     * 修改操作资源
     */
    BaseElement updateAction(BaseElement action);

    /**
     * 移除操作
     */
    void removeAction(Long actionId);

    /**
     * 移除菜单相关资源
     */
    void removeByMenuId(Long menuId);
}