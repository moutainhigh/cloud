package com.smart4y.cloud.base.domain.service;

import com.smart4y.cloud.base.domain.model.*;
import com.smart4y.cloud.base.domain.repository.BaseAuthorityActionMapper;
import com.smart4y.cloud.base.domain.repository.BaseAuthorityAppMapper;
import com.smart4y.cloud.base.domain.repository.BaseAuthorityRoleMapper;
import com.smart4y.cloud.base.domain.repository.BaseAuthorityUserMapper;
import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import com.smart4y.cloud.core.infrastructure.security.OpenSecurityConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    @Autowired
    public BaseAuthorityDomainService(BaseAuthorityActionMapper baseAuthorityActionMapper, BaseAuthorityAppMapper baseAuthorityAppMapper, BaseAuthorityRoleMapper baseAuthorityRoleMapper, BaseAuthorityUserMapper baseAuthorityUserMapper) {
        this.baseAuthorityActionMapper = baseAuthorityActionMapper;
        this.baseAuthorityAppMapper = baseAuthorityAppMapper;
        this.baseAuthorityRoleMapper = baseAuthorityRoleMapper;
        this.baseAuthorityUserMapper = baseAuthorityUserMapper;
    }

    /**
     * 新增或更新权限
     *
     * @param baseApis API资源列表
     */
    public void modifyAuthorityApis(List<BaseApi> baseApis) {
        if (CollectionUtils.isNotEmpty(baseApis)) {
            List<Long> apiIds = baseApis.stream().map(BaseApi::getApiId).collect(Collectors.toList());
            Map<Long, BaseAuthority> authorities = getApiBaseAuthorities(apiIds).stream()
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
     * 移除所有API资源的权限
     *
     * @param apiIds API资源列表
     */
    public void removeApiAuthorities(List<Long> apiIds) {
        List<Long> authorityIds = this.getApiBaseAuthorities(apiIds).stream()
                .map(BaseAuthority::getAuthorityId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(authorityIds)) {
            this.removeAuthorityActions(authorityIds);
            this.removeAuthorityApps(authorityIds);
            this.removeAuthorityRoles(authorityIds);
            this.removeAuthorityUsers(authorityIds);
            this.removeAuthorities(authorityIds);
        }
    }

    /**
     * 移除权限操作
     */
    private void removeAuthorityActions(List<Long> authorityIds) {
        Weekend<BaseAuthorityAction> weekend = Weekend.of(BaseAuthorityAction.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthorityAction::getAuthorityId, authorityIds);
        baseAuthorityActionMapper.deleteByExample(weekend);
    }

    /**
     * 移除权限应用
     */
    private void removeAuthorityApps(List<Long> authorityIds) {
        Weekend<BaseAuthorityApp> weekend = Weekend.of(BaseAuthorityApp.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthorityApp::getAuthorityId, authorityIds);
        baseAuthorityAppMapper.deleteByExample(weekend);
    }

    /**
     * 移除权限角色
     */
    private void removeAuthorityRoles(List<Long> authorityIds) {
        Weekend<BaseAuthorityRole> weekend = Weekend.of(BaseAuthorityRole.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthorityRole::getAuthorityId, authorityIds);
        baseAuthorityRoleMapper.deleteByExample(weekend);
    }

    /**
     * 移除权限用户
     */
    private void removeAuthorityUsers(List<Long> authorityIds) {
        Weekend<BaseAuthorityUser> weekend = Weekend.of(BaseAuthorityUser.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthorityUser::getAuthorityId, authorityIds);
        baseAuthorityUserMapper.deleteByExample(weekend);
    }

    /**
     * 移除基础权限
     */
    private void removeAuthorities(List<Long> authorityIds) {
        Weekend<BaseAuthority> weekend = Weekend.of(BaseAuthority.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthority::getAuthorityId, authorityIds);
        this.remove(weekend);
    }

    /**
     * 查询API资源的基础权限
     *
     * @param apiIds API资源列表
     */
    private List<BaseAuthority> getApiBaseAuthorities(List<Long> apiIds) {
        if (CollectionUtils.isEmpty(apiIds)) {
            return Collections.emptyList();
        }
        Weekend<BaseAuthority> weekend = Weekend.of(BaseAuthority.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthority::getApiId, apiIds);
        return this.list(weekend);
    }
}