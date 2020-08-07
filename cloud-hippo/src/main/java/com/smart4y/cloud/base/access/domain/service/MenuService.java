package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.model.RbacMenu;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Youtao
 * on 2020/8/7 14:55
 */
@DomainService
public class MenuService extends BaseDomainService<RbacMenu> {

    public List<RbacMenu> getMenus(Collection<Long> menuIds) {
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
}