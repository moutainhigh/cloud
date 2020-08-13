package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.model.RbacPrivilegeMenu;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Youtao
 * on 2020/8/7 14:51
 */
@DomainService
public class PrivilegeMenuService extends BaseDomainService<RbacPrivilegeMenu> {

    public Optional<RbacPrivilegeMenu> getPrivilege(long menuId) {
        Weekend<RbacPrivilegeMenu> weekend = Weekend.of(RbacPrivilegeMenu.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacPrivilegeMenu::getMenuId, menuId);
        return Optional.ofNullable(this.getOne(weekend));
    }

    public List<RbacPrivilegeMenu> getMenus(Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilegeMenu> weekend = Weekend.of(RbacPrivilegeMenu.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilegeMenu::getPrivilegeId, privilegeIds);
        return this.list(weekend);
    }

    public List<RbacPrivilegeMenu> getPrivileges(Collection<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilegeMenu> weekend = Weekend.of(RbacPrivilegeMenu.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilegeMenu::getMenuId, menuIds);
        return this.list(weekend);
    }

    public void removeByPrivilege(Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return;
        }
        Weekend<RbacPrivilegeMenu> weekend = Weekend.of(RbacPrivilegeMenu.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilegeMenu::getPrivilegeId, privilegeIds);
        this.remove(weekend);
    }
}