package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseApiService;
import com.smart4y.cloud.base.application.BaseAuthorityService;
import com.smart4y.cloud.base.domain.model.BaseApi;
import com.smart4y.cloud.base.domain.repository.BaseApiMapper;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.Page;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.constants.ResourceType;
import com.smart4y.cloud.core.infrastructure.exception.OpenAlertException;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@ApplicationService
public class BaseApiServiceImpl implements BaseApiService {

    @Autowired
    private BaseApiMapper baseApiMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    @Override
    public IPage<BaseApi> findListPage(PageParams pageParams) {
        BaseApi query = pageParams.mapToObject(BaseApi.class);
        Weekend<BaseApi> queryWrapper = Weekend.of(BaseApi.class);
        WeekendCriteria<BaseApi, Object> criteria = queryWrapper.weekendCriteria();
        if (StringUtils.isNotBlank(query.getPath())) {
            criteria.andLike(BaseApi::getPath, query.getPath() + "%");
        }
        if (StringUtils.isNotBlank(query.getApiName())) {
            criteria.andLike(BaseApi::getApiName, query.getApiName() + "%");
        }
        if (StringUtils.isNotBlank(query.getApiCode())) {
            criteria.andLike(BaseApi::getApiCode, query.getApiCode() + "%");
        }
        if (StringUtils.isNotBlank(query.getServiceId())) {
            criteria.andEqualTo(BaseApi::getServiceId, query.getServiceId());
        }
        if (null != query.getStatus()) {
            criteria.andEqualTo(BaseApi::getStatus, query.getStatus());
        }
        if (null != query.getIsAuth()) {
            criteria.andEqualTo(BaseApi::getIsAuth, query.getIsAuth());
        }
        queryWrapper.orderBy("createdDate").desc();

        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), Boolean.TRUE);
        List<BaseApi> list = baseApiMapper.selectByExample(queryWrapper);
        PageInfo<BaseApi> pageInfo = new PageInfo<>(list);
        IPage<BaseApi> page = new Page<>();
        page.setRecords(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }

    @Override
    public List<BaseApi> findAllList(String serviceId) {
        Weekend<BaseApi> queryWrapper = Weekend.of(BaseApi.class);
        WeekendCriteria<BaseApi, Object> criteria = queryWrapper.weekendCriteria();
        if (StringUtils.isNotBlank(serviceId)) {
            criteria.andEqualTo(BaseApi::getServiceId, serviceId);
        }
        return baseApiMapper.selectByExample(queryWrapper);
    }

    @Override
    public BaseApi getApi(long apiId) {
        return baseApiMapper.selectByPrimaryKey(apiId);
    }

    @Override
    public Boolean isExist(String apiCode) {
        Weekend<BaseApi> queryWrapper = Weekend.of(BaseApi.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseApi::getApiCode, apiCode);
        int count = baseApiMapper.selectCountByExample(queryWrapper);
        return count > 0;
    }

    @Override
    public void addApi(BaseApi api) {
        if (isExist(api.getApiCode())) {
            throw new OpenAlertException(String.format("%s编码已存在!", api.getApiCode()));
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
        baseApiMapper.insert(api);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(api.getApiId(), ResourceType.api);
    }

    @Override
    public void updateApi(BaseApi api) {
        BaseApi saved = getApi(api.getApiId());
        if (saved == null) {
            throw new OpenAlertException("信息不存在!");
        }
        if (!saved.getApiCode().equals(api.getApiCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(api.getApiCode())) {
                throw new OpenAlertException(String.format("%s编码已存在!", api.getApiCode()));
            }
        }
        if (api.getPriority() == null) {
            api.setPriority(0);
        }
        if (api.getApiCategory() == null) {
            api.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
        }
        api.setLastModifiedDate(LocalDateTime.now());
        baseApiMapper.updateByPrimaryKeySelective(api);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(api.getApiId(), ResourceType.api);
    }

    @Override
    public BaseApi getApi(String apiCode) {
        Weekend<BaseApi> queryWrapper = Weekend.of(BaseApi.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseApi::getApiCode, apiCode);
        return baseApiMapper.selectOneByExample(queryWrapper);
    }


    @Override
    public void removeApi(Long apiId) {
        BaseApi api = getApi(apiId);
        if (api != null && api.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException("保留数据，不允许删除");
        }
        baseAuthorityService.removeAuthority(apiId, ResourceType.api);
        baseApiMapper.deleteByPrimaryKey(apiId);
    }

    @Override
    public void removeApis(List<Long> apiIds) {
        Weekend<BaseApi> queryWrapper = Weekend.of(BaseApi.class);
        queryWrapper.weekendCriteria()
                .andIn(BaseApi::getApiId, apiIds)
                .andEqualTo(BaseApi::getIsPersist, BaseConstants.DISABLED);
        baseApiMapper.deleteByExample(queryWrapper);
    }

    @Override
    public void updateOpenStatusApis(int isOpen, List<Long> apiIds) {
        Weekend<BaseApi> wrapper = Weekend.of(BaseApi.class);
        wrapper.weekendCriteria()
                .andIn(BaseApi::getApiId, apiIds);
        BaseApi entity = new BaseApi();
        entity.setIsOpen(isOpen);
        baseApiMapper.updateByExampleSelective(entity, wrapper);
    }

    @Override
    public void updateStatusApis(int status, List<Long> apiIds) {
        Weekend<BaseApi> queryWrapper = Weekend.of(BaseApi.class);
        queryWrapper.weekendCriteria()
                .andIn(BaseApi::getApiId, apiIds);
        BaseApi entity = new BaseApi();
        entity.setStatus(status);
        baseApiMapper.updateByExampleSelective(entity, queryWrapper);
    }

    @Override
    public void updateAuthApis(int auth, List<Long> apiIds) {
        Weekend<BaseApi> wrapper = Weekend.of(BaseApi.class);
        wrapper.weekendCriteria()
                .andIn(BaseApi::getApiId, apiIds)
                .andEqualTo(BaseApi::getIsPersist, BaseConstants.DISABLED);
        BaseApi entity = new BaseApi();
        entity.setStatus(auth);
        baseApiMapper.updateByExampleSelective(entity, wrapper);
    }
}