package com.smart4y.cloud.base.domain.service;

import com.smart4y.cloud.base.domain.model.BaseApi;
import com.smart4y.cloud.base.domain.model.BaseAuthority;
import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import com.smart4y.cloud.core.infrastructure.security.OpenSecurityConstants;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.time.LocalDateTime;
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

    /**
     * 新增或更新权限
     *
     * @param baseApis API资源列表
     */
    public void modifyAuthorityApis(List<BaseApi> baseApis) {
        if (CollectionUtils.isNotEmpty(baseApis)) {
            List<Long> apiIds = baseApis.stream().map(BaseApi::getApiId).collect(Collectors.toList());
            Map<Long, BaseAuthority> authorities = getBaseAuthorityApis(apiIds).stream()
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

    private List<BaseAuthority> getBaseAuthorityApis(List<Long> apiIds) {
        Weekend<BaseAuthority> weekend = Weekend.of(BaseAuthority.class);
        weekend
                .weekendCriteria()
                .andIn(BaseAuthority::getApiId, apiIds);
        return this.list(weekend);
    }
}