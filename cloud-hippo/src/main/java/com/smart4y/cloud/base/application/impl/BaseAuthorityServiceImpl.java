package com.smart4y.cloud.base.application.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.smart4y.cloud.base.application.*;
import com.smart4y.cloud.base.domain.model.*;
import com.smart4y.cloud.base.domain.repository.*;
import com.smart4y.cloud.core.application.ApplicationService;
import com.smart4y.cloud.core.domain.OpenAuthority;
import com.smart4y.cloud.core.infrastructure.constants.CommonConstants;
import com.smart4y.cloud.core.infrastructure.constants.ResourceType;
import com.smart4y.cloud.core.infrastructure.exception.OpenAlertException;
import com.smart4y.cloud.core.infrastructure.exception.OpenException;
import com.smart4y.cloud.core.infrastructure.security.OpenHelper;
import com.smart4y.cloud.core.infrastructure.security.OpenSecurityConstants;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtils;
import com.smart4y.cloud.core.interfaces.AuthorityApiDTO;
import com.smart4y.cloud.core.interfaces.AuthorityMenuDTO;
import com.smart4y.cloud.core.interfaces.AuthorityResourceDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统权限管理
 * 对菜单、操作、API等进行权限分配操作
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class BaseAuthorityServiceImpl implements BaseAuthorityService {

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    @Autowired
    private BaseAuthorityMapper baseAuthorityMapper;
    @Autowired
    private BaseAuthorityRoleMapper baseAuthorityRoleMapper;
    @Autowired
    private BaseAuthorityRoleCustomMapper baseAuthorityRoleCustomMapper;
    @Autowired
    private BaseAuthorityUserMapper baseAuthorityUserMapper;
    @Autowired
    private BaseAuthorityUserCustomMapper baseAuthorityUserCustomMapper;
    @Autowired
    private BaseAuthorityAppMapper baseAuthorityAppMapper;
    @Autowired
    private BaseAuthorityAppCustomMapper baseAuthorityAppCustomMapper;
    @Autowired
    private BaseAuthorityCustomMapper baseAuthorityCustomMapper;
    @Autowired
    private BaseAuthorityActionMapper baseAuthorityActionMapper;
    @Autowired
    private BaseMenuService baseMenuService;
    @Autowired
    private BaseActionService baseActionService;
    @Autowired
    private BaseApiMapper baseApiMapper;
    @Autowired
    private BaseApiService baseApiService;
    @Autowired
    private BaseRoleService baseRoleService;
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private BaseAppService baseAppService;
    @Autowired
    private RedisTokenStore redisTokenStore;

    @Override
    public List<AuthorityResourceDTO> findAuthorityResource() {
        List<AuthorityResourceDTO> list = Lists.newArrayList();
        // 已授权资源权限
        List<AuthorityResourceDTO> resourceList = baseAuthorityCustomMapper.selectAllAuthorityResource();
        if (resourceList != null) {
            list.addAll(resourceList);
        }
        return list;
    }

    @Override
    public List<AuthorityMenuDTO> findAuthorityMenu(Integer status) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("status", status);
        List<AuthorityMenuDTO> authorities = baseAuthorityCustomMapper.selectAuthorityMenu(map);
        authorities.sort(Comparator.comparing(AuthorityMenuDTO::getPriority));
        return authorities;

    }

    @Override
    public List<AuthorityApiDTO> findAuthorityApi(String serviceId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("serviceId", serviceId);
        map.put("status", 1);
        return baseAuthorityCustomMapper.selectAuthorityApi(map);
    }

    @Override
    public List<BaseAuthorityAction> findAuthorityAction(Long actionId) {
        Weekend<BaseAuthorityAction> queryWrapper = Weekend.of(BaseAuthorityAction.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseAuthorityAction::getActionId, actionId);
        return baseAuthorityActionMapper.selectByExample(queryWrapper);
    }

    @Override
    public BaseAuthority saveOrUpdateAuthority(Long resourceId, ResourceType resourceType) {
        BaseAuthority baseAuthority = getAuthority(resourceId, resourceType);
        String authority = null;
        if (baseAuthority == null) {
            baseAuthority = new BaseAuthority();
        }
        if (ResourceType.menu.equals(resourceType)) {
            BaseMenu menu = baseMenuService.getMenu(resourceId);
            authority = OpenSecurityConstants.AUTHORITY_PREFIX_MENU + menu.getMenuCode();
            baseAuthority.setMenuId(resourceId);
            baseAuthority.setStatus(menu.getStatus());
        }
        if (ResourceType.action.equals(resourceType)) {
            BaseAction operation = baseActionService.getAction(resourceId);
            authority = OpenSecurityConstants.AUTHORITY_PREFIX_ACTION + operation.getActionCode();
            baseAuthority.setActionId(resourceId);
            baseAuthority.setStatus(operation.getStatus());
        }
        if (ResourceType.api.equals(resourceType)) {
            BaseApi api = baseApiService.getApi(resourceId);
            authority = OpenSecurityConstants.AUTHORITY_PREFIX_API + api.getApiCode();
            baseAuthority.setApiId(resourceId);
            baseAuthority.setStatus(api.getStatus());
        }
        if (authority == null) {
            return null;
        }
        // 设置权限标识
        baseAuthority.setAuthority(authority);
        if (baseAuthority.getAuthorityId() == null) {
            baseAuthority.setCreatedDate(LocalDateTime.now());
            baseAuthority.setLastModifiedDate(LocalDateTime.now());
            // 新增权限
            baseAuthorityMapper.insert(baseAuthority);
        } else {
            // 修改权限
            baseAuthority.setLastModifiedDate(LocalDateTime.now());
            baseAuthorityMapper.updateByPrimaryKeySelective(baseAuthority);
        }
        return baseAuthority;
    }

    @Override
    public void removeAuthority(Long resourceId, ResourceType resourceType) {
        if (isGranted(resourceId, resourceType)) {
            throw new OpenAlertException("资源已被授权，不允许删除！取消授权后，再次尝试！");
        }
        Weekend<BaseAuthority> queryWrapper = buildQueryWrapper(resourceId, resourceType);
        baseAuthorityMapper.deleteByExample(queryWrapper);
    }

    @Override
    public BaseAuthority getAuthority(Long resourceId, ResourceType resourceType) {
        if (resourceId == null || resourceType == null) {
            return null;
        }
        Weekend<BaseAuthority> queryWrapper = buildQueryWrapper(resourceId, resourceType);
        return baseAuthorityMapper.selectOneByExample(queryWrapper);
    }

    @Override
    public Boolean isGranted(Long resourceId, ResourceType resourceType) {
        BaseAuthority authority = getAuthority(resourceId, resourceType);
        if (authority == null || authority.getAuthorityId() == null) {
            return false;
        }
        Weekend<BaseAuthorityRole> roleQueryWrapper = Weekend.of(BaseAuthorityRole.class);
        roleQueryWrapper.weekendCriteria()
                .andEqualTo(BaseAuthorityRole::getAuthorityId, authority.getAuthorityId());
        int roleGrantedCount = baseAuthorityRoleMapper.selectCountByExample(roleQueryWrapper);

        Weekend<BaseAuthorityUser> userQueryWrapper = Weekend.of(BaseAuthorityUser.class);
        userQueryWrapper.weekendCriteria()
                .andEqualTo(BaseAuthorityUser::getAuthorityId, authority.getAuthorityId());
        int userGrantedCount = baseAuthorityUserMapper.selectCountByExample(userQueryWrapper);

        Weekend<BaseAuthorityApp> appQueryWrapper = Weekend.of(BaseAuthorityApp.class);
        appQueryWrapper.weekendCriteria()
                .andEqualTo(BaseAuthorityApp::getAuthorityId, authority.getAuthorityId());
        int appGrantedCount = baseAuthorityAppMapper.selectCountByExample(appQueryWrapper);

        return roleGrantedCount > 0 || userGrantedCount > 0 || appGrantedCount > 0;
    }

    private Weekend<BaseAuthority> buildQueryWrapper(Long resourceId, ResourceType resourceType) {
        Weekend<BaseAuthority> queryWrapper = Weekend.of(BaseAuthority.class);
        WeekendCriteria<BaseAuthority, Object> criteria = queryWrapper.weekendCriteria();
        if (ResourceType.menu.equals(resourceType)) {
            criteria.andEqualTo(BaseAuthority::getMenuId, resourceId);
        }
        if (ResourceType.action.equals(resourceType)) {
            criteria.andEqualTo(BaseAuthority::getActionId, resourceId);
        }
        if (ResourceType.api.equals(resourceType)) {
            criteria.andEqualTo(BaseAuthority::getApiId, resourceId);
        }
        return queryWrapper;
    }

    @Override
    public void removeAuthorityApp(String appId) {
        Weekend<BaseAuthorityApp> queryWrapper = Weekend.of(BaseAuthorityApp.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseAuthorityApp::getAppId, appId);
        baseAuthorityAppMapper.deleteByExample(queryWrapper);
    }

    @Override
    public void removeAuthorityAction(Long actionId) {
        Weekend<BaseAuthorityAction> queryWrapper = Weekend.of(BaseAuthorityAction.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseAuthorityAction::getActionId, actionId);
        baseAuthorityActionMapper.deleteByExample(queryWrapper);
    }

    @Override
    public void addAuthorityRole(Long roleId, LocalDateTime expireTime, List<Long> authorityIds) {
        if (roleId == null) {
            return;
        }
        // 清空角色已有授权
        Weekend<BaseAuthorityRole> roleQueryWrapper = Weekend.of(BaseAuthorityRole.class);
        roleQueryWrapper.weekendCriteria()
                .andEqualTo(BaseAuthorityRole::getRoleId, roleId);
        baseAuthorityRoleMapper.deleteByExample(roleQueryWrapper);
        if (CollectionUtils.isNotEmpty(authorityIds)) {
            BaseAuthorityRole authority;
            for (Long id : authorityIds) {
                authority = new BaseAuthorityRole();
                authority.setAuthorityId(id);
                authority.setRoleId(roleId);
                authority.setExpireTime(expireTime);
                authority.setCreatedDate(LocalDateTime.now());
                authority.setLastModifiedDate(LocalDateTime.now());
                // 批量添加授权
                baseAuthorityRoleMapper.insert(authority);
            }
        }
    }

    @Override
    public void addAuthorityUser(Long userId, LocalDateTime expireTime, List<Long> authorityIds) {
        if (userId == null) {
            return;
        }
        BaseUser user = baseUserService.getUserById(userId);
        if (user == null) {
            return;
        }
        if (CommonConstants.ROOT.equals(user.getUserName())) {
            throw new OpenAlertException("默认用户无需授权!");
        }
        // 获取用户角色列表
        List<Long> roleIds = baseRoleService.getUserRoleIds(userId);
        // 清空用户已有授权
        // 清空角色已有授权
        Weekend<BaseAuthorityUser> userQueryWrapper = Weekend.of(BaseAuthorityUser.class);
        userQueryWrapper.weekendCriteria()
                .andEqualTo(BaseAuthorityUser::getUserId, userId);
        baseAuthorityUserMapper.deleteByExample(userQueryWrapper);
        if (CollectionUtils.isNotEmpty(authorityIds)) {
            BaseAuthorityUser authority;
            for (Long id : authorityIds) {
                if (roleIds != null && roleIds.size() > 0) {
                    // 防止重复授权
                    if (isGrantedByRoleIds(id, roleIds)) {
                        continue;
                    }
                }
                authority = new BaseAuthorityUser();
                authority.setAuthorityId(id);
                authority.setUserId(userId);
                authority.setExpireTime(expireTime);
                authority.setCreatedDate(LocalDateTime.now());
                authority.setLastModifiedDate(LocalDateTime.now());
                baseAuthorityUserMapper.insert(authority);
            }
        }
    }

    @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    @Override
    public void addAuthorityApp(String appId, LocalDateTime expireTime, List<Long> authorityIds) {
        if (appId == null) {
            return;
        }
        BaseApp baseApp = baseAppService.getAppInfo(appId);
        if (baseApp == null) {
            return;
        }
        // 清空应用已有授权
        Weekend<BaseAuthorityApp> appQueryWrapper = Weekend.of(BaseAuthorityApp.class);
        appQueryWrapper.weekendCriteria()
                .andEqualTo(BaseAuthorityApp::getAppId, appId);
        baseAuthorityAppMapper.deleteByExample(appQueryWrapper);
        if (CollectionUtils.isNotEmpty(authorityIds)) {
            BaseAuthorityApp authority;
            for (Long id : authorityIds) {
                authority = new BaseAuthorityApp();
                authority.setAuthorityId(id);
                authority.setAppId(appId);
                authority.setExpireTime(expireTime);
                authority.setCreatedDate(LocalDateTime.now());
                authority.setLastModifiedDate(LocalDateTime.now());
                baseAuthorityAppMapper.insert(authority);

            }
        }
        // 获取应用最新的权限列表
        List<OpenAuthority> authorities = findAuthorityByApp(appId);
        // 动态更新tokenStore客户端
        OpenHelper.updateOpenClientAuthorities(redisTokenStore, baseApp.getApiKey(), authorities);
    }

    @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    @Override
    public void addAuthorityApp(String appId, LocalDateTime expireTime, String authorityId) {
        BaseAuthorityApp authority = new BaseAuthorityApp();
        authority.setAppId(appId);
        authority.setAuthorityId(Long.parseLong(authorityId));
        authority.setExpireTime(expireTime);
        authority.setCreatedDate(LocalDateTime.now());
        authority.setLastModifiedDate(LocalDateTime.now());
        Weekend<BaseAuthorityApp> appQueryWrapper = Weekend.of(BaseAuthorityApp.class);
        appQueryWrapper.weekendCriteria()
                .andEqualTo(BaseAuthorityApp::getAppId, appId)
                .andEqualTo(BaseAuthorityApp::getAuthorityId, authorityId);
        int count = baseAuthorityAppMapper.selectCountByExample(appQueryWrapper);
        if (count > 0) {
            return;
        }
        authority.setCreatedDate(LocalDateTime.now());
        baseAuthorityAppMapper.insert(authority);
    }

    @Override
    public void addAuthorityAction(Long actionId, List<Long> authorityIds) {
        if (actionId == null) {
            return;
        }
        // 移除操作已绑定接口
        removeAuthorityAction(actionId);
        if (CollectionUtils.isNotEmpty(authorityIds)) {
            for (Long id : authorityIds) {
                BaseAuthorityAction authority = new BaseAuthorityAction();
                authority.setActionId(actionId);
                authority.setAuthorityId(id);
                authority.setCreatedDate(LocalDateTime.now());
                authority.setLastModifiedDate(LocalDateTime.now());
                baseAuthorityActionMapper.insert(authority);
            }
        }
    }

    @Override
    public List<OpenAuthority> findAuthorityByApp(String appId) {
        List<OpenAuthority> authorities = Lists.newArrayList();
        List<OpenAuthority> list = baseAuthorityAppCustomMapper.selectAuthorityByApp(appId);
        if (list != null) {
            authorities.addAll(list);
        }
        return authorities;
    }

    @Override
    public List<OpenAuthority> findAuthorityByRole(Long roleId) {
        return baseAuthorityRoleCustomMapper.selectAuthorityByRole(roleId);
    }

    @Override
    public List<OpenAuthority> findAuthorityByType(String type) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("type", type);
        map.put("status", 1);
        return baseAuthorityCustomMapper.selectAuthorityAll(map);
    }

    @Override
    public List<OpenAuthority> findAuthorityByUser(Long userId, Boolean root) {
        if (root) {
            // 超级管理员返回所有
            return findAuthorityByType("1");
        }
        List<OpenAuthority> authorities = Lists.newArrayList();
        List<BaseRole> rolesList = baseRoleService.getUserRoles(userId);
        if (rolesList != null) {
            for (BaseRole role : rolesList) {
                // 加入角色已授权
                List<OpenAuthority> roleGrantedAuthority = findAuthorityByRole(role.getRoleId());
                if (roleGrantedAuthority != null && roleGrantedAuthority.size() > 0) {
                    authorities.addAll(roleGrantedAuthority);
                }
            }
        }
        // 加入用户特殊授权
        List<OpenAuthority> userGrantedAuthority = baseAuthorityUserCustomMapper.selectAuthorityByUser(userId);
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        // 权限去重
        Set h = new HashSet<>(authorities);
        authorities.clear();
        authorities.addAll(h);
        return authorities;
    }

    @Override
    public List<AuthorityMenuDTO> findAuthorityMenuByUser(Long userId, Boolean root) {
        if (root) {
            // 超级管理员返回所有
            return findAuthorityMenu(null);
        }
        // 用户权限列表
        List<AuthorityMenuDTO> authorities = Lists.newArrayList();
        List<BaseRole> rolesList = baseRoleService.getUserRoles(userId);
        if (rolesList != null) {
            for (BaseRole role : rolesList) {
                // 加入角色已授权
                List<AuthorityMenuDTO> roleGrantedAuthority = baseAuthorityRoleCustomMapper.selectAuthorityMenuByRole(role.getRoleId());
                if (roleGrantedAuthority != null && roleGrantedAuthority.size() > 0) {
                    authorities.addAll(roleGrantedAuthority);
                }
            }
        }
        // 加入用户特殊授权
        List<AuthorityMenuDTO> userGrantedAuthority = baseAuthorityUserCustomMapper.selectAuthorityMenuByUser(userId);
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        // 权限去重
        Set h = new HashSet<>(authorities);
        authorities.clear();
        authorities.addAll(h);
        //根据优先级从小到大排序
        authorities.sort(Comparator.comparing(AuthorityMenuDTO::getPriority));
        return authorities;
    }

    @Override
    public boolean isGrantedByRoleIds(Long authorityId, List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            throw new OpenException("roleIds is empty");
        }
        Weekend<BaseAuthorityRole> roleQueryWrapper = Weekend.of(BaseAuthorityRole.class);
        roleQueryWrapper.weekendCriteria()
                .andIn(BaseAuthorityRole::getRoleId, roleIds)
                .andEqualTo(BaseAuthorityRole::getAuthorityId, authorityId);
        int count = baseAuthorityRoleMapper.selectCountByExample(roleQueryWrapper);
        return count > 0;
    }

    @Override
    public void clearInvalidApi(String serviceId, Collection<String> codes) {
        if (StringUtils.isBlank(serviceId)) {
            return;
        }
        Weekend<BaseApi> apiWeekend = Weekend.of(BaseApi.class);
        WeekendCriteria<BaseApi, Object> criteria = apiWeekend.weekendCriteria();
        criteria.andEqualTo(BaseApi::getServiceId, serviceId);
        if (CollectionUtils.isNotEmpty(codes)) {
            criteria.andNotIn(BaseApi::getApiCode, codes);
        }
        List<Long> invalidApiIds = baseApiMapper.selectByExample(apiWeekend).stream()
                .map(BaseApi::getApiId)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(invalidApiIds)) {
            // 防止删除默认API
            invalidApiIds.remove(1L);
            invalidApiIds.remove(2L);
            // 获取无效的权限
            if (invalidApiIds.isEmpty()) {
                return;
            }

            Weekend<BaseAuthority> authorityWeekend = Weekend.of(BaseAuthority.class);
            authorityWeekend.weekendCriteria()
                    .andIn(BaseAuthority::getApiId, invalidApiIds);
            List<Long> invalidAuthorityIds = baseAuthorityMapper.selectByExample(authorityWeekend).stream()
                    .map(BaseAuthority::getAuthorityId)
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(invalidAuthorityIds)) {
                // 移除关联数据
                Weekend<BaseAuthorityApp> appWeekend = Weekend.of(BaseAuthorityApp.class);
                appWeekend.weekendCriteria().andIn(BaseAuthorityApp::getAuthorityId, invalidAuthorityIds);
                baseAuthorityAppMapper.deleteByExample(appWeekend);
                Weekend<BaseAuthorityAction> actionWeekend = Weekend.of(BaseAuthorityAction.class);
                actionWeekend.weekendCriteria().andIn(BaseAuthorityAction::getAuthorityId, invalidAuthorityIds);
                baseAuthorityActionMapper.deleteByExample(actionWeekend);
                Weekend<BaseAuthorityRole> roleWeekend = Weekend.of(BaseAuthorityRole.class);
                roleWeekend.weekendCriteria().andIn(BaseAuthorityRole::getAuthorityId, invalidAuthorityIds);
                baseAuthorityRoleMapper.deleteByExample(roleWeekend);
                Weekend<BaseAuthorityUser> userWeekend = Weekend.of(BaseAuthorityUser.class);
                userWeekend.weekendCriteria().andIn(BaseAuthorityUser::getAuthorityId, invalidAuthorityIds);
                baseAuthorityUserMapper.deleteByExample(userWeekend);

                // 移除权限数据
                Weekend<BaseAuthority> authorityDeleteWeekend = Weekend.of(BaseAuthority.class);
                authorityDeleteWeekend.weekendCriteria()
                        .andIn(BaseAuthority::getAuthorityId, invalidAuthorityIds);
                baseAuthorityMapper.deleteByExample(authorityDeleteWeekend);

                // 移除接口资源
                Weekend<BaseApi> apiDeleteWeekend = Weekend.of(BaseApi.class);
                apiDeleteWeekend.weekendCriteria()
                        .andIn(BaseApi::getApiId, invalidApiIds);
                baseApiMapper.deleteByExample(apiDeleteWeekend);
            }
        }
    }
}