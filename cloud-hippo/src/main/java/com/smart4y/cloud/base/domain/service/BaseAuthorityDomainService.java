package com.smart4y.cloud.base.domain.service;

import com.google.common.collect.Lists;
import com.smart4y.cloud.base.domain.model.*;
import com.smart4y.cloud.base.domain.repository.*;
import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;
import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.domain.model.OpenAuthority;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.constants.ResourceType;
import com.smart4y.cloud.core.infrastructure.exception.OpenAlertException;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import com.smart4y.cloud.core.infrastructure.security.OpenSecurityConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 权限服务
 *
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@DomainService
public class BaseAuthorityDomainService extends BaseDomainService<BaseAuthority> {

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    private final BaseAuthorityActionMapper baseAuthorityActionMapper;
    private final BaseAuthorityAppMapper baseAuthorityAppMapper;
    private final BaseAuthorityRoleMapper baseAuthorityRoleMapper;
    private final BaseAuthorityUserMapper baseAuthorityUserMapper;
    private final BaseCustomMapper baseCustomMapper;
    private final BaseRoleDomainService baseRoleDomainService;
    private final BaseMenuMapper baseMenuMapper;
    private final BaseActionMapper baseActionMapper;

    @Autowired
    public BaseAuthorityDomainService(BaseAuthorityActionMapper baseAuthorityActionMapper, BaseAuthorityAppMapper baseAuthorityAppMapper, BaseAuthorityRoleMapper baseAuthorityRoleMapper, BaseAuthorityUserMapper baseAuthorityUserMapper, BaseCustomMapper baseCustomMapper, BaseRoleDomainService baseRoleDomainService, BaseMenuMapper baseMenuMapper, BaseActionMapper baseActionMapper) {
        this.baseAuthorityActionMapper = baseAuthorityActionMapper;
        this.baseAuthorityAppMapper = baseAuthorityAppMapper;
        this.baseAuthorityRoleMapper = baseAuthorityRoleMapper;
        this.baseAuthorityUserMapper = baseAuthorityUserMapper;
        this.baseCustomMapper = baseCustomMapper;
        this.baseRoleDomainService = baseRoleDomainService;
        this.baseMenuMapper = baseMenuMapper;
        this.baseActionMapper = baseActionMapper;
    }

    /**
     * 新增或更新权限（权限表API数据）
     *
     * @param baseApis API资源列表
     */
    public void modifyAuthoritiesForApi(List<BaseApi> baseApis) {
        if (CollectionUtils.isNotEmpty(baseApis)) {
            List<Long> apiIds = baseApis.stream().map(BaseApi::getApiId).collect(Collectors.toList());
            Map<Long, BaseAuthority> authorities = getAuthoritiesForApi(apiIds).stream()
                    .collect(Collectors.toMap(BaseAuthority::getApiId, Function.identity()));

            LocalDateTime now = LocalDateTime.now();
            List<BaseAuthority> items = baseApis.stream()
                    .map(api -> {
                        BaseAuthority record = new BaseAuthority();
                        if (authorities.containsKey(api.getApiId())) {
                            record = authorities.get(api.getApiId())
                                    .setLastModifiedDate(now);
                        } else {
                            record
                                    .setApiId(api.getApiId())
                                    .setAuthority(OpenSecurityConstants.AUTHORITY_PREFIX_API + api.getApiCode())
                                    .setStatus(api.getStatus())
                                    .setCreatedDate(now)
                                    .setLastModifiedDate(now);
                        }
                        return record;
                    })
                    .collect(Collectors.toList());
            this.saveOrUpdateBatch(items);
        }
    }

    /**
     * 移除 所有API权限（权限表API数据所关联的所有权限[action/app/role/user]）
     *
     * @param apiIds API资源列表
     */
    public void removeAuthoritiesForApi(List<Long> apiIds) {
        List<Long> authorityIds = this.getAuthoritiesForApi(apiIds).stream()
                .map(BaseAuthority::getAuthorityId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(authorityIds)) {
            this.removeActionAuthorities(authorityIds);
            this.removeAppAuthorities(authorityIds);
            this.removeRoleAuthorities(authorityIds);
            this.removeUserAuthorities(authorityIds);

            this.removeAuthorities(authorityIds);
        }
    }

    /**
     * 获取 API基础权限
     *
     * @param apiIds API资源列表
     */
    private List<BaseAuthority> getAuthoritiesForApi(List<Long> apiIds) {
        if (CollectionUtils.isEmpty(apiIds)) {
            return Collections.emptyList();
        }
        Weekend<BaseAuthority> weekend = Weekend.of(BaseAuthority.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthority::getApiId, apiIds);
        return this.list(weekend);
    }

    private BaseAuthority getAuthority(long resourceId, ResourceType resourceType) {
        Weekend<BaseAuthority> weekend = Weekend.of(BaseAuthority.class);
        WeekendCriteria<BaseAuthority, Object> criteria = weekend.weekendCriteria();
        switch (resourceType) {
            case api:
                criteria.andEqualTo(BaseAuthority::getApiId, resourceId);
                break;
            case menu:
                criteria.andEqualTo(BaseAuthority::getMenuId, resourceId);
                break;
            case action:
                criteria.andEqualTo(BaseAuthority::getActionId, resourceId);
                break;
        }
        return this.getOne(weekend);
    }

    /**
     * 移除 操作权限
     */
    private void removeActionAuthorities(List<Long> authorityIds) {
        Weekend<BaseAuthorityAction> weekend = Weekend.of(BaseAuthorityAction.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthorityAction::getAuthorityId, authorityIds);
        baseAuthorityActionMapper.deleteByExample(weekend);
    }

    /**
     * 获取 应用权限
     */
    public List<OpenAuthority> getAppAuthorities(String appId) {
        return baseCustomMapper.selectAppAuthorities(appId);
    }

    /**
     * 移除 应用权限
     */
    private void removeAppAuthorities(List<Long> authorityIds) {
        Weekend<BaseAuthorityApp> weekend = Weekend.of(BaseAuthorityApp.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthorityApp::getAuthorityId, authorityIds);
        baseAuthorityAppMapper.deleteByExample(weekend);
    }

    /**
     * 获取 角色权限
     */
    private List<OpenAuthority> getRoleAuthorities(long roleId) {
        return baseCustomMapper.selectRoleAuthorities(roleId);
    }

    /**
     * 移除 角色权限
     */
    private void removeRoleAuthorities(List<Long> authorityIds) {
        Weekend<BaseAuthorityRole> weekend = Weekend.of(BaseAuthorityRole.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthorityRole::getAuthorityId, authorityIds);
        baseAuthorityRoleMapper.deleteByExample(weekend);
    }

    /**
     * 获取 用户权限
     *
     * @param userId  用户ID
     * @param isAdmin 是否管理员（true是admin false非admin）
     */
    public List<OpenAuthority> getUserAuthorities(long userId, boolean isAdmin) {
        if (isAdmin) {
            // 超级管理员返回所有
            return baseCustomMapper.selectAdminAuthorities();
        }

        List<OpenAuthority> authorities = Lists.newArrayList();
        // 加入角色权限
        List<BaseRole> userRoles = baseRoleDomainService.getUserRoles(userId);
        for (BaseRole role : userRoles) {
            List<OpenAuthority> roleAuthorities = this.getRoleAuthorities(role.getRoleId());
            if (CollectionUtils.isNotEmpty(roleAuthorities)) {
                authorities.addAll(roleAuthorities);
            }
        }
        // 加入用户特殊授权
        List<OpenAuthority> userAuthorities = baseCustomMapper.selectUserAuthorities(userId);
        if (CollectionUtils.isNotEmpty(userAuthorities)) {
            authorities.addAll(userAuthorities);
        }
        // 权限去重
        Set<OpenAuthority> set = new HashSet<>(authorities);
        authorities.clear();
        authorities.addAll(set);

        return authorities;
    }

    /**
     * 移除 用户权限
     */
    private void removeUserAuthorities(List<Long> authorityIds) {
        Weekend<BaseAuthorityUser> weekend = Weekend.of(BaseAuthorityUser.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthorityUser::getAuthorityId, authorityIds);
        baseAuthorityUserMapper.deleteByExample(weekend);
    }

    /**
     * 移除 基础权限
     */
    private void removeAuthorities(List<Long> authorityIds) {
        Weekend<BaseAuthority> weekend = Weekend.of(BaseAuthority.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthority::getAuthorityId, authorityIds);
        this.remove(weekend);
    }

    /**
     * 获取 所有资源授权列表
     */
    public List<AuthorityResourceDTO> getAuthorityResources() {
        return baseCustomMapper.selectAllAuthorityResource();
    }

    /**
     * 获取 菜单权限
     */
    public List<AuthorityMenuDTO> getMenuAuthoritiesAll() {
        return baseCustomMapper.selectMenuAuthoritiesAll();
    }

    /**
     * 获取 角色菜单权限
     */
    public List<AuthorityMenuDTO> getRoleMenuAuthorities(long roleId) {
        return baseCustomMapper.selectRoleMenuAuthorities(roleId);
    }

    /**
     * 获取 用户菜单权限
     */
    public List<AuthorityMenuDTO> getUserMenuAuthorities(long userId) {
        return baseCustomMapper.selectUserMenuAuthorities(userId);
    }

    /**
     * 获取 所有菜单
     */
    public List<BaseMenu> getMenus() {
        Weekend<BaseMenu> weekend = Weekend.of(BaseMenu.class);
        weekend
                .weekendCriteria()
                .andEqualTo(BaseMenu::getStatus, BaseConstants.ENABLED);
        weekend
                .orderBy("priority").asc();
        return baseMenuMapper.selectByExample(weekend);
    }

    /**
     * 获取菜单下所有操作
     */
    public List<BaseAction> getMenuActionsByMenuId(long menuId) {
        Weekend<BaseAction> weekend = Weekend.of(BaseAction.class);
        weekend
                .weekendCriteria()
                .andEqualTo(BaseAction::getMenuId, menuId);
        weekend
                .orderBy("priority").asc();
        return baseActionMapper.selectByExample(weekend);
    }

    /**
     * 获取菜单资源详情
     */
    public BaseMenu getMenu(long menuId) {
        return baseMenuMapper.selectByPrimaryKey(menuId);
    }

    /**
     * 添加菜单
     */
    public long addMenu(BaseMenu menu) {
        if (menuCodeExist(menu.getMenuCode())) {
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
        baseMenuMapper.insertSelective(menu);

        this.modifyAuthorityForMenu(menu.getMenuId());

        return menu.getMenuId();
    }

    /**
     * 新增或更新权限（Menu 权限）
     */
    private void modifyAuthorityForMenu(long menuId) {
        BaseMenu menu = getMenu(menuId);
        BaseAuthority baseAuthority = this.getAuthority(menu.getMenuId(), ResourceType.menu);
        if (null == baseAuthority) {
            baseAuthority = new BaseAuthority()
                    .setMenuId(menu.getMenuId())
                    .setStatus(menu.getStatus())
                    .setAuthority(OpenSecurityConstants.AUTHORITY_PREFIX_MENU + menu.getMenuCode());
        }
        if (null == baseAuthority.getAuthorityId()) {
            baseAuthority
                    .setCreatedDate(LocalDateTime.now())
                    .setLastModifiedDate(LocalDateTime.now());
            this.save(baseAuthority);
        } else {
            baseAuthority.setLastModifiedDate(LocalDateTime.now());
            this.updateSelectiveById(baseAuthority);
        }
    }

    /**
     * 菜单代码是否存在
     */
    private boolean menuCodeExist(String menuCode) {
        Weekend<BaseMenu> weekend = Weekend.of(BaseMenu.class);
        weekend
                .weekendCriteria()
                .andEqualTo(BaseMenu::getMenuCode, menuCode);
        return baseMenuMapper.selectCountByExample(weekend) > 0;
    }

    /**
     * 修改菜单
     */
    public void updateMenu(BaseMenu menu) {
        BaseMenu saved = getMenu(menu.getMenuId());
        if (saved == null) {
            throw new OpenAlertException(String.format("%s信息不存在!", menu.getMenuId()));
        }
        if (!saved.getMenuCode().equals(menu.getMenuCode())) {
            // 和原来不一致重新检查唯一性
            if (menuCodeExist(menu.getMenuCode())) {
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

        this.modifyAuthorityForMenu(menu.getMenuId());
    }

    /**
     * 移除菜单
     */
    public void removeMenu(long menuId) {
        BaseMenu menu = getMenu(menuId);
        if (menu != null && menu.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException("保留数据,不允许删除!");
        }
        BaseAuthority authority = getAuthority(menuId, ResourceType.menu);
        if (null != authority) {
            this.removeById(authority.getAuthorityId());
        }
        //        // 移除菜单权限
        //        baseAuthorityService.removeAuthority(menuId, ResourceType.menu);
        //        // 移除功能按钮和相关权限
        //        baseActionService.removeByMenuId(menuId);
        // 移除菜单信息
        baseMenuMapper.deleteByPrimaryKey(menuId);
    }
}