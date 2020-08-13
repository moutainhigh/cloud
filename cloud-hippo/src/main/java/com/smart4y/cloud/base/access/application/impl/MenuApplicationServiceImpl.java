package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.MenuApplicationService;
import com.smart4y.cloud.base.access.domain.event.MenuCodeChangedEvent;
import com.smart4y.cloud.base.access.domain.event.MenuCreatedEvent;
import com.smart4y.cloud.base.access.domain.event.MenuParentChangedEvent;
import com.smart4y.cloud.base.access.domain.event.MenuRemovedEvent;
import com.smart4y.cloud.base.access.domain.entity.RbacMenu;
import com.smart4y.cloud.base.access.domain.service.MenuService;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.ModifyMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.RbacMenuQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.exception.OpenAlertException;
import com.smart4y.cloud.core.message.enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import tk.mybatis.mapper.weekend.Weekend;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Youtao
 * on 2020/8/6 10:45
 */
@Slf4j
@ApplicationService
public class MenuApplicationServiceImpl implements MenuApplicationService {

    @Autowired
    private MenuService menuService;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public List<RbacMenu> getMenuChildren(RbacMenuQuery query) {
        Weekend<RbacMenu> weekend = Weekend.of(RbacMenu.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacMenu::getMenuParentId, query.getMenuId());
        weekend
                .orderBy("menuSorted").asc();
        return menuService.list(weekend);
    }

    @Override
    public RbacMenu viewMenu(long menuId) {
        return menuService.getById(menuId);
    }

    @Override
    public List<RbacMenu> getMenus() {
        return menuService.list();
    }

    @Override
    public void createMenu(CreateMenuCommand command) {
        String menuCode = command.getMenuCode();
        boolean existMenuCode = menuService.existMenuCode(menuCode);
        if (existMenuCode) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "菜单编码已存在：" + menuCode);
        }

        RbacMenu record = new RbacMenu();
        BeanUtils.copyProperties(command, record);
        record.setCreatedDate(LocalDateTime.now());
        menuService.save(record);

        long menuParentId = record.getMenuParentId();
        menuService.modifyExistChild(menuParentId);

        publisher.publishEvent(new MenuCreatedEvent(record.getMenuId(), record.getMenuCode()));
    }

    @Override
    public void modifyMenu(long menuId, ModifyMenuCommand command) {
        if (menuId == command.getMenuParentId()) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "父菜单不能为自身");
        }

        RbacMenu oldMenu = menuService.getById(menuId);
        String oldMenuCode = oldMenu.getMenuCode();
        long oldMenuParentId = oldMenu.getMenuParentId();

        RbacMenu record = new RbacMenu();
        BeanUtils.copyProperties(command, record);
        record.setMenuId(menuId);
        record.setLastModifiedDate(LocalDateTime.now());
        menuService.updateSelectiveById(record);

        // 父级改变
        if (oldMenuParentId != record.getMenuParentId()) {
            this.modifyExistChild(oldMenuParentId);
            menuService.modifyExistChild(record.getMenuParentId());
            publisher.publishEvent(new MenuParentChangedEvent(menuId, command.getMenuParentId(), oldMenuParentId));
        }
        // 菜单编码改变
        if (!oldMenuCode.equals(record.getMenuCode())) {
            publisher.publishEvent(new MenuCodeChangedEvent(menuId, command.getMenuCode(), oldMenuCode));
        }
    }

    @Override
    public void removeMenu(long menuId) {
        RbacMenu rbacMenu = menuService.getById(menuId);
        if (null == rbacMenu || rbacMenu.getExistChild()) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "当前菜单存在子节点，禁止删除");
        }

        menuService.removeById(menuId);

        // 当前菜单父级是否还有子节点（即当前菜单是否有同级）
        modifyExistChild(rbacMenu.getMenuParentId());

        publisher.publishEvent(new MenuRemovedEvent(menuId));
    }

    /**
     * 更新`是否子节点`字段
     *
     * @param menuParentId 当前菜单的父级ID
     */
    private void modifyExistChild(long menuParentId) {
        if (0 == menuParentId) {
            return;
        }
        boolean hasChild = menuService.hasChild(menuParentId);
        if (!hasChild) {
            menuService.updateChild(menuParentId, false);
        }
    }
}