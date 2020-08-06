package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.model.RbacMenu;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.CreateMenuCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.menu.RbacMenuQuery;

import java.util.List;

/**
 * @author Youtao
 * on 2020/8/6 10:43
 */
public interface MenuApplicationService {

    List<RbacMenu> getMenuChildren(RbacMenuQuery query);

    RbacMenu viewMenu(long menuId);

    List<RbacMenu> getMenus();

    void createMenu(CreateMenuCommand command);
}
