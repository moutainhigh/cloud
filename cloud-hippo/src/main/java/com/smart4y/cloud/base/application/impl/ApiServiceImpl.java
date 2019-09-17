package com.smart4y.cloud.base.application.impl;

import com.smart4y.cloud.base.application.ApiService;
import com.smart4y.cloud.base.domain.model.BaseApi;
import com.smart4y.cloud.base.domain.service.BaseApiDomainService;
import com.smart4y.cloud.base.domain.service.BaseAuthorityDomainService;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源应用服务
 *
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Slf4j
@ApplicationService
public class ApiServiceImpl implements ApiService {

    private final BaseApiDomainService baseApiDomainService;
    private final BaseAuthorityDomainService baseAuthorityDomainService;

    @Autowired
    public ApiServiceImpl(BaseApiDomainService baseApiDomainService, BaseAuthorityDomainService baseAuthorityDomainService) {
        this.baseApiDomainService = baseApiDomainService;
        this.baseAuthorityDomainService = baseAuthorityDomainService;
    }

    @Override
    public void modifyApis(List<BaseApi> apis) {
        // 新增操作
        List<BaseApi> addApis = apis.stream()
                .filter(api -> null == api.getApiId())
                .collect(Collectors.toList());
        baseApiDomainService.addApis(addApis);
        // 更新操作
        List<BaseApi> updateApis = apis.stream()
                .filter(api -> null != api.getApiId())
                .collect(Collectors.toList());
        baseApiDomainService.updateApis(updateApis);
        // 同步权限
        List<String> apiCodes = apis.stream().map(BaseApi::getApiCode).collect(Collectors.toList());
        List<BaseApi> baseApis = baseApiDomainService.getApis(apiCodes);
        baseAuthorityDomainService.modifyAuthorityApis(baseApis);
    }

    @Override
    public void clearInvalidApis(String serviceId, List<String> apiCodes) {
        if (CollectionUtils.isNotEmpty(apiCodes)) {
            // TODO

        }
    }
}