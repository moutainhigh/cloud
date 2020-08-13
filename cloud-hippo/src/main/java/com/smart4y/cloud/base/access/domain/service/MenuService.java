package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacMenu;
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

    public List<RbacMenu> getAllStateMenus(Collection<Long> menuIds) {
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

    public List<RbacMenu> getByCodes(Collection<String> menuCodes) {
        if (CollectionUtils.isEmpty(menuCodes)) {
            return Collections.emptyList();
        }
        Weekend<RbacMenu> weekend = Weekend.of(RbacMenu.class);
        weekend
                .weekendCriteria()
                .andIn(RbacMenu::getMenuCode, menuCodes);
        return this.list(weekend);
    }

    public boolean existMenuCode(String menuCode) {
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
     */
    public boolean hasChild(long menuId) {
        Weekend<RbacMenu> weekend = Weekend.of(RbacMenu.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacMenu::getMenuParentId, menuId);
        weekend
                .setOrderByClause("menu_id DESC LIMIT 1");
        return this.exist(weekend);
    }

    public void modifyExistChild(long menuId) {
        this.updateChild(menuId, true);
    }

    public void updateNotExistChild(long menuId) {
        this.updateChild(menuId, false);
    }

    public void updateChild(long menuId, boolean hasChild) {
        if (0 == menuId) {
            return;
        }
        RbacMenu record = new RbacMenu()
                .setMenuId(menuId)
                .setExistChild(hasChild)
                .setLastModifiedDate(LocalDateTime.now());
        this.updateSelectiveById(record);
    }
}