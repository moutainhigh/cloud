package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.MenuApplicationService;
import com.smart4y.cloud.base.access.domain.model.RbacMenu;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.ModifyMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.RbacMenuQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.mapper.BaseDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Youtao
 * on 2020/8/6 10:45
 */
@Slf4j
@ApplicationService
public class MenuApplicationServiceImpl extends BaseDomainService<RbacMenu> implements MenuApplicationService {

    @Override
    public List<RbacMenu> getMenuChildren(RbacMenuQuery query) {
        Weekend<RbacMenu> weekend = Weekend.of(RbacMenu.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacMenu::getMenuParentId, query.getMenuId());
        weekend
                .orderBy("menuSorted").asc();
        return this.list(weekend);
    }

    @Override
    public RbacMenu viewMenu(long menuId) {
        return this.getById(menuId);
    }

    @Override
    public List<RbacMenu> getMenus() {
        return this.list();
    }

    @Override
    public void createMenu(CreateMenuCommand command) {
        RbacMenu record = new RbacMenu();
        BeanUtils.copyProperties(command, record);
        this.save(record);
    }

    @Override
    public void modifyMenu(long menuId, ModifyMenuCommand command) {
        RbacMenu record = new RbacMenu()
                .setMenuId(menuId)
                .setMenuParentId(command.getMenuParentId())
                .setMenuName(command.getMenuName())
                .setMenuCode(command.getMenuCode())
                .setMenuIcon(command.getMenuIcon())
                .setMenuPath(command.getMenuPath())
                .setMenuSchema(command.getMenuSchema())
                .setMenuTarget(command.getMenuTarget())
                .setMenuState(command.getMenuState())
                .setMenuSorted(command.getMenuSorted())
                .setLastModifiedDate(LocalDateTime.now());
        this.updateSelectiveById(record);
    }
}