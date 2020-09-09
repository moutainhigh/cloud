package com.smart4y.cloud.hippo.domain.service;

import com.smart4y.cloud.hippo.domain.entity.RbacMenu;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Youtao
 * on 2020/8/7 14:55
 */
@DomainService
public class MenuService extends BaseDomainService<RbacMenu> {

    /**
     * 获取指定菜单列表
     *
     * @param menuIds 菜单ID列表
     * @return 菜单列表
     */
    public List<RbacMenu> getByIds(Collection<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacMenu> weekend = Weekend.of(RbacMenu.class);
        weekend
                .weekendCriteria()
                .andIn(RbacMenu::getMenuId, menuIds);
        weekend
                .orderBy("menuSorted").asc();
        return this.list(weekend);
    }

    /**
     * 获取指定菜单的子节点
     *
     * @param parentId 父级ID
     * @return 子节点菜单列表
     */
    public List<RbacMenu> getChildrenByParentId(long parentId) {
        Weekend<RbacMenu> weekend = Weekend.of(RbacMenu.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacMenu::getMenuParentId, parentId);
        weekend
                .orderBy("menuSorted").asc();
        return this.list(weekend);
    }

    /**
     * 指定菜单是否存在
     *
     * @param menuCode 菜单标识
     * @return true存在，false不存在
     */
    public boolean existByCode(String menuCode) {
        Weekend<RbacMenu> weekend = Weekend.of(RbacMenu.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacMenu::getMenuCode, menuCode);
        weekend
                .setOrderByClause("menu_id DESC LIMIT 1");
        return this.exist(weekend);
    }

    /**
     * 是否存在子节点
     *
     * @param menuId 菜单ID，当传入的值为`0`是返回`true`
     * @return true存在，false不存在
     */
    public boolean hasChild(long menuId) {
        if (0 == menuId) {
            return true;
        }
        Weekend<RbacMenu> weekend = Weekend.of(RbacMenu.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacMenu::getMenuParentId, menuId);
        weekend
                .setOrderByClause("menu_id DESC LIMIT 1");
        return this.exist(weekend);
    }

    /**
     * 更新`子节点状态`为`存在子节点`
     *
     * @param menuId 菜单ID
     */
    public void modifyChildForExist(long menuId) {
        if (0 == menuId) {
            return;
        }
        RbacMenu record = new RbacMenu()
                .setMenuId(menuId)
                .setExistChild(true)
                .setLastModifiedDate(LocalDateTime.now());
        this.updateSelectiveById(record);
    }

    /**
     * 更新`子节点状态`为`不存在子节点`
     *
     * @param menuId 菜单ID
     */
    public void modifyChildForNotExist(long menuId) {
        if (0 == menuId) {
            return;
        }
        RbacMenu record = new RbacMenu()
                .setMenuId(menuId)
                .setExistChild(false)
                .setLastModifiedDate(LocalDateTime.now());
        this.updateSelectiveById(record);
    }
}