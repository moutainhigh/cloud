package com.smart4y.cloud.base.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.domain.model.BaseMenu;
import com.smart4y.cloud.core.domain.PageParams;

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
    PageInfo<BaseMenu> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<BaseMenu> findAllList();

    /**
     * 根据主键获取菜单
     *
     * @param menuId
     * @return
     */
    BaseMenu getMenu(long menuId);

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode
     * @return
     */
    Boolean isExist(String menuCode);


    /**
     * 添加菜单资源
     *
     * @param menu
     * @return
     */
    BaseMenu addMenu(BaseMenu menu);

    /**
     * 修改菜单资源
     *
     * @param menu
     * @return
     */
    BaseMenu updateMenu(BaseMenu menu);

    /**
     * 移除菜单
     *
     * @param menuId
     * @return
     */
    void removeMenu(Long menuId);
}
