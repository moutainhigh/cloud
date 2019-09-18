package com.smart4y.cloud.base.domain.service;

import com.google.common.collect.Lists;
import com.smart4y.cloud.base.domain.model.*;
import com.smart4y.cloud.base.domain.repository.*;
import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;
import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.domain.model.OpenAuthority;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import com.smart4y.cloud.core.infrastructure.security.OpenSecurityConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;

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

    private final BaseAuthorityActionMapper baseAuthorityActionMapper;
    private final BaseAuthorityAppMapper baseAuthorityAppMapper;
    private final BaseAuthorityRoleMapper baseAuthorityRoleMapper;
    private final BaseAuthorityUserMapper baseAuthorityUserMapper;
    private final BaseCustomMapper baseCustomMapper;
    private final BaseRoleDomainService baseRoleDomainService;

    @Autowired
    public BaseAuthorityDomainService(BaseAuthorityActionMapper baseAuthorityActionMapper, BaseAuthorityAppMapper baseAuthorityAppMapper, BaseAuthorityRoleMapper baseAuthorityRoleMapper, BaseAuthorityUserMapper baseAuthorityUserMapper, BaseCustomMapper baseCustomMapper, BaseRoleDomainService baseRoleDomainService) {
        this.baseAuthorityActionMapper = baseAuthorityActionMapper;
        this.baseAuthorityAppMapper = baseAuthorityAppMapper;
        this.baseAuthorityRoleMapper = baseAuthorityRoleMapper;
        this.baseAuthorityUserMapper = baseAuthorityUserMapper;
        this.baseCustomMapper = baseCustomMapper;
        this.baseRoleDomainService = baseRoleDomainService;
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
}