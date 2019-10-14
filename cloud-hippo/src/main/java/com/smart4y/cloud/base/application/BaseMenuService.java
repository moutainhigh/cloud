package com.smart4y.cloud.base.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.domain.model.BaseMenu;
import com.smart4y.cloud.base.interfaces.valueobject.query.BaseMenuQuery;

import java.util.List;

/**
 * 菜单资源管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface BaseMenuService {

    /**
     * 分页查询
     */
    PageInfo<BaseMenu> findListPage(BaseMenuQuery query);

    /**
     * 查询列表
     */
    List<BaseMenu> findAllList();

    /**
     * 根据主键获取菜单
     */
    BaseMenu getMenu(long menuId);

    /**
     * 检查菜单编码是否存在
     */
    Boolean isExist(String menuCode);

    /**
     * 添加菜单资源
     */
    BaseMenu addMenu(BaseMenu menu);

    /**
     * 修改菜单资源
     */
    BaseMenu updateMenu(BaseMenu menu);

    /**
     * 移除菜单
     */
    void removeMenu(Long menuId);
}