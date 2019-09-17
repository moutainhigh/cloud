package com.smart4y.cloud.base.domain.service;

import com.smart4y.cloud.base.domain.model.BaseApi;
import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.weekend.Weekend;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@DomainService
public class BaseApiDomainService extends BaseDomainService<BaseApi> {

    /**
     * 查询 API列表
     */
    public List<BaseApi> getApis(List<String> apiCodes) {
        if (CollectionUtils.isEmpty(apiCodes)) {
            return Collections.emptyList();
        }
        Weekend<BaseApi> weekend = Weekend.of(BaseApi.class);
        weekend
                .weekendCriteria()
                .andIn(BaseApi::getApiCode, apiCodes);
        return this.list(weekend);
    }

    /**
     * 新增API资源
     */
    public void addApis(List<BaseApi> apis) {
        if (CollectionUtils.isNotEmpty(apis)) {
            List<BaseApi> items = apis.stream()
                    .peek(api -> {
                        if (api.getPriority() == null) {
                            api.setPriority(0);
                        }
                        if (api.getStatus() == null) {
                            api.setStatus(BaseConstants.ENABLED);
                        }
                        if (api.getApiCategory() == null) {
                            api.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
                        }
                        if (api.getIsPersist() == null) {
                            api.setIsPersist(0);
                        }
                        if (api.getIsAuth() == null) {
                            api.setIsAuth(0);
                        }
                        api.setCreatedDate(LocalDateTime.now());
                    }).collect(Collectors.toList());
            this.saveBatch(items);
        }
    }

    public void updateApis(List<BaseApi> apis) {
        if (CollectionUtils.isNotEmpty(apis)) {
            List<BaseApi> items = apis.stream()
                    .peek(api -> {
                        if (api.getPriority() == null) {
                            api.setPriority(0);
                        }
                        if (api.getApiCategory() == null) {
                            api.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
                        }
                        api.setLastModifiedDate(LocalDateTime.now());
                    }).collect(Collectors.toList());
            this.updateSelectiveBatchById(items);
        }
    }
}