package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.model.RbacMenu;

import java.util.List;

/**
 * 当前登录用户操作接口
 *
 * @author Youtao
 * on 2020/8/7 14:24
 */
public interface CurrentApplicationService {

    List<RbacMenu> getCurrentUserMenus(long userId);
}