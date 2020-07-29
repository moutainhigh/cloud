package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseResourceService;
import com.smart4y.cloud.base.application.BaseAuthorityService;
import com.smart4y.cloud.base.domain.model.BaseResource;
import com.smart4y.cloud.base.infrastructure.mapper.BaseResourceMapper;
import com.smart4y.cloud.base.interfaces.query.BaseResourceQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.constant.BaseConstants;
import com.smart4y.cloud.core.constant.ResourceType;
import com.smart4y.cloud.core.exception.OpenAlertException;
import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.toolkit.base.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class BaseResourceServiceImpl implements BaseResourceService {

    @Autowired
    private BaseResourceMapper baseResourceMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    @Override
    public PageInfo<BaseResource> findListPage(BaseResourceQuery query) {
        Weekend<BaseResource> queryWrapper = Weekend.of(BaseResource.class);
        WeekendCriteria<BaseResource, Object> criteria = queryWrapper.weekendCriteria();
        if (StringHelper.isNotBlank(query.getPath())) {
            criteria.andLike(BaseResource::getPath, query.getPath() + "%");
        }
        if (StringHelper.isNotBlank(query.getApiName())) {
            criteria.andLike(BaseResource::getApiName, query.getApiName() + "%");
        }
        if (StringHelper.isNotBlank(query.getApiCode())) {
            criteria.andLike(BaseResource::getApiCode, query.getApiCode() + "%");
        }
        if (StringHelper.isNotBlank(query.getServiceId())) {
            criteria.andEqualTo(BaseResource::getServiceId, query.getServiceId());
        }
        if (null != query.getStatus()) {
            criteria.andEqualTo(BaseResource::getStatus, query.getStatus());
        }
        if (null != query.getIsAuth()) {
            criteria.andEqualTo(BaseResource::getIsAuth, query.getIsAuth());
        }
        queryWrapper.orderBy("createdDate").desc();

        PageHelper.startPage(query.getPage(), query.getLimit(), Boolean.TRUE);
        List<BaseResource> list = baseResourceMapper.selectByExample(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public List<BaseResource> findAllList(String serviceId) {
        Weekend<BaseResource> queryWrapper = Weekend.of(BaseResource.class);
        WeekendCriteria<BaseResource, Object> criteria = queryWrapper.weekendCriteria();
        if (StringHelper.isNotBlank(serviceId)) {
            criteria.andEqualTo(BaseResource::getServiceId, serviceId);
        }
        return baseResourceMapper.selectByExample(queryWrapper);
    }

    @Override
    public BaseResource getApi(long apiId) {
        return baseResourceMapper.selectByPrimaryKey(apiId);
    }

    @Override
    public Boolean isExist(String apiCode) {
        Weekend<BaseResource> queryWrapper = Weekend.of(BaseResource.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseResource::getApiCode, apiCode);
        int count = baseResourceMapper.selectCountByExample(queryWrapper);
        return count > 0;
    }

    @Override
    public void addApi(BaseResource api) {
        if (isExist(api.getApiCode())) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, String.format("%s编码已存在!", api.getApiCode()));
        }
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
        api.setLastModifiedDate(LocalDateTime.now());
        baseResourceMapper.insert(api);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(api.getApiId(), ResourceType.api);
    }

    @Override
    public void updateApi(BaseResource api) {
        BaseResource saved = getApi(api.getApiId());
        if (saved == null) {
            throw new OpenAlertException(MessageType.NOT_FOUND, "信息不存在!");
        }
        if (!saved.getApiCode().equals(api.getApiCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(api.getApiCode())) {
                throw new OpenAlertException(MessageType.BAD_REQUEST, String.format("%s编码已存在!", api.getApiCode()));
            }
        }
        if (api.getPriority() == null) {
            api.setPriority(0);
        }
        if (api.getApiCategory() == null) {
            api.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
        }
        api.setLastModifiedDate(LocalDateTime.now());
        baseResourceMapper.updateByPrimaryKeySelective(api);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(api.getApiId(), ResourceType.api);
    }

    @Override
    public BaseResource getApi(String apiCode) {
        Weekend<BaseResource> queryWrapper = Weekend.of(BaseResource.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseResource::getApiCode, apiCode);
        return baseResourceMapper.selectOneByExample(queryWrapper);
    }


    @Override
    public void removeApi(Long apiId) {
        BaseResource api = getApi(apiId);
        if (api != null && api.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "保留数据，不允许删除");
        }
        baseAuthorityService.removeAuthority(apiId, ResourceType.api);
        baseResourceMapper.deleteByPrimaryKey(apiId);
    }

    @Override
    public void removeApis(List<Long> apiIds) {
        Weekend<BaseResource> queryWrapper = Weekend.of(BaseResource.class);
        queryWrapper.weekendCriteria()
                .andIn(BaseResource::getApiId, apiIds)
                .andEqualTo(BaseResource::getIsPersist, BaseConstants.DISABLED);
        baseResourceMapper.deleteByExample(queryWrapper);
    }

    @Override
    public void updateOpenStatusApis(int isOpen, List<Long> apiIds) {
        Weekend<BaseResource> wrapper = Weekend.of(BaseResource.class);
        wrapper.weekendCriteria()
                .andIn(BaseResource::getApiId, apiIds);
        BaseResource entity = new BaseResource();
        entity.setIsOpen(isOpen);
        baseResourceMapper.updateByExampleSelective(entity, wrapper);
    }

    @Override
    public void updateStatusApis(int status, List<Long> apiIds) {
        Weekend<BaseResource> queryWrapper = Weekend.of(BaseResource.class);
        queryWrapper.weekendCriteria()
                .andIn(BaseResource::getApiId, apiIds);
        BaseResource entity = new BaseResource();
        entity.setStatus(status);
        baseResourceMapper.updateByExampleSelective(entity, queryWrapper);
    }

    @Override
    public void updateAuthApis(int auth, List<Long> apiIds) {
        Weekend<BaseResource> wrapper = Weekend.of(BaseResource.class);
        wrapper.weekendCriteria()
                .andIn(BaseResource::getApiId, apiIds)
                .andEqualTo(BaseResource::getIsPersist, BaseConstants.DISABLED);
        BaseResource entity = new BaseResource();
        entity.setStatus(auth);
        baseResourceMapper.updateByExampleSelective(entity, wrapper);
    }
}