package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseActionService;
import com.smart4y.cloud.base.application.BaseAuthorityService;
import com.smart4y.cloud.base.application.BaseMenuService;
import com.smart4y.cloud.base.domain.model.BaseMenu;
import com.smart4y.cloud.base.domain.repository.BaseMenuMapper;
import com.smart4y.cloud.base.interfaces.valueobject.query.BaseMenuQuery;
import com.smart4y.cloud.core.application.ApplicationService;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.constants.ResourceType;
import com.smart4y.cloud.core.infrastructure.exception.OpenAlertException;
import com.smart4y.cloud.core.infrastructure.toolkit.base.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class BaseMenuServiceImpl implements BaseMenuService {

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    @Autowired
    private BaseMenuMapper baseMenuMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private BaseActionService baseActionService;

    @Override
    public PageInfo<BaseMenu> findListPage(BaseMenuQuery query) {
        Weekend<BaseMenu> queryWrapper = Weekend.of(BaseMenu.class);
        WeekendCriteria<BaseMenu, Object> criteria = queryWrapper.weekendCriteria();
        if (StringHelper.isNotBlank(query.getMenuCode())) {
            criteria.andLike(BaseMenu::getMenuCode, query.getMenuCode() + "%");
        }
        if (StringHelper.isNotBlank(query.getMenuName())) {
            criteria.andLike(BaseMenu::getMenuName, query.getMenuName() + "%");
        }

        PageHelper.startPage(query.getPage(), query.getLimit(), Boolean.TRUE);
        List<BaseMenu> list = baseMenuMapper.selectByExample(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public List<BaseMenu> findAllList() {
        List<BaseMenu> list = baseMenuMapper.selectAll();
        // 根据优先级从小到大排序
        list.sort(Comparator.comparing(BaseMenu::getPriority));
        return list;
    }

    @Override
    public BaseMenu getMenu(long menuId) {
        return baseMenuMapper.selectByPrimaryKey(menuId);
    }

    @Override
    public Boolean isExist(String menuCode) {
        Weekend<BaseMenu> queryWrapper = Weekend.of(BaseMenu.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseMenu::getMenuCode, menuCode);
        int count = baseMenuMapper.selectCountByExample(queryWrapper);
        return count > 0;
    }

    @Override
    public BaseMenu addMenu(BaseMenu menu) {
        if (isExist(menu.getMenuCode())) {
            throw new OpenAlertException(String.format("%s编码已存在!", menu.getMenuCode()));
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getPriority() == null) {
            menu.setPriority(0);
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        if (menu.getIsPersist() == null) {
            menu.setIsPersist(0);
        }
        menu.setServiceId(DEFAULT_SERVICE_ID);
        menu.setCreatedDate(LocalDateTime.now());
        menu.setLastModifiedDate(LocalDateTime.now());
        baseMenuMapper.insert(menu);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(menu.getMenuId(), ResourceType.menu);
        return menu;
    }

    @Override
    public BaseMenu updateMenu(BaseMenu menu) {
        BaseMenu saved = getMenu(menu.getMenuId());
        if (saved == null) {
            throw new OpenAlertException(String.format("%s信息不存在!", menu.getMenuId()));
        }
        if (!saved.getMenuCode().equals(menu.getMenuCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(menu.getMenuCode())) {
                throw new OpenAlertException(String.format("%s编码已存在!", menu.getMenuCode()));
            }
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getPriority() == null) {
            menu.setPriority(0);
        }
        menu.setLastModifiedDate(LocalDateTime.now());
        baseMenuMapper.updateByPrimaryKeySelective(menu);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(menu.getMenuId(), ResourceType.menu);
        return menu;
    }

    @Override
    public void removeMenu(Long menuId) {
        BaseMenu menu = getMenu(menuId);
        if (menu != null && menu.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException("保留数据，不允许删除！");
        }
        // 移除菜单权限
        baseAuthorityService.removeAuthority(menuId, ResourceType.menu);
        // 移除功能按钮和相关权限
        baseActionService.removeByMenuId(menuId);
        // 移除菜单信息
        baseMenuMapper.deleteByPrimaryKey(menuId);
    }
}