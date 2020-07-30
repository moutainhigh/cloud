package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseOperationService;
import com.smart4y.cloud.base.application.BaseAuthorityService;
import com.smart4y.cloud.base.domain.model.BaseOperation;
import com.smart4y.cloud.base.infrastructure.mapper.BaseOperationMapper;
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
public class BaseOperationServiceImpl implements BaseOperationService {

    @Autowired
    private BaseOperationMapper baseOperationMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    @Override
    public PageInfo<BaseOperation> findListPage(BaseResourceQuery query) {
        Weekend<BaseOperation> queryWrapper = Weekend.of(BaseOperation.class);
        WeekendCriteria<BaseOperation, Object> criteria = queryWrapper.weekendCriteria();
        if (StringHelper.isNotBlank(query.getPath())) {
            criteria.andLike(BaseOperation::getPath, query.getPath() + "%");
        }
        if (StringHelper.isNotBlank(query.getApiName())) {
            criteria.andLike(BaseOperation::getApiName, query.getApiName() + "%");
        }
        if (StringHelper.isNotBlank(query.getApiCode())) {
            criteria.andLike(BaseOperation::getApiCode, query.getApiCode() + "%");
        }
        if (StringHelper.isNotBlank(query.getServiceId())) {
            criteria.andEqualTo(BaseOperation::getServiceId, query.getServiceId());
        }
        if (null != query.getStatus()) {
            criteria.andEqualTo(BaseOperation::getStatus, query.getStatus());
        }
        if (null != query.getIsAuth()) {
            criteria.andEqualTo(BaseOperation::getIsAuth, query.getIsAuth());
        }
        queryWrapper.orderBy("createdDate").desc();

        PageHelper.startPage(query.getPage(), query.getLimit(), Boolean.TRUE);
        List<BaseOperation> list = baseOperationMapper.selectByExample(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public List<BaseOperation> findAllList(String serviceId) {
        Weekend<BaseOperation> queryWrapper = Weekend.of(BaseOperation.class);
        WeekendCriteria<BaseOperation, Object> criteria = queryWrapper.weekendCriteria();
        if (StringHelper.isNotBlank(serviceId)) {
            criteria.andEqualTo(BaseOperation::getServiceId, serviceId);
        }
        return baseOperationMapper.selectByExample(queryWrapper);
    }

    @Override
    public BaseOperation getApi(long apiId) {
        return baseOperationMapper.selectByPrimaryKey(apiId);
    }

    @Override
    public Boolean isExist(String apiCode) {
        Weekend<BaseOperation> queryWrapper = Weekend.of(BaseOperation.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseOperation::getApiCode, apiCode);
        int count = baseOperationMapper.selectCountByExample(queryWrapper);
        return count > 0;
    }

    @Override
    public void addApi(BaseOperation api) {
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
        baseOperationMapper.insert(api);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(api.getApiId(), ResourceType.api);
    }

    @Override
    public void updateApi(BaseOperation api) {
        BaseOperation saved = getApi(api.getApiId());
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
        baseOperationMapper.updateByPrimaryKeySelective(api);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(api.getApiId(), ResourceType.api);
    }

    @Override
    public BaseOperation getApi(String apiCode) {
        Weekend<BaseOperation> queryWrapper = Weekend.of(BaseOperation.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseOperation::getApiCode, apiCode);
        return baseOperationMapper.selectOneByExample(queryWrapper);
    }


    @Override
    public void removeApi(Long apiId) {
        BaseOperation api = getApi(apiId);
        if (api != null && api.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "保留数据，不允许删除");
        }
        baseAuthorityService.removeAuthority(apiId, ResourceType.api);
        baseOperationMapper.deleteByPrimaryKey(apiId);
    }

    @Override
    public void removeApis(List<Long> apiIds) {
        Weekend<BaseOperation> queryWrapper = Weekend.of(BaseOperation.class);
        queryWrapper.weekendCriteria()
                .andIn(BaseOperation::getApiId, apiIds)
                .andEqualTo(BaseOperation::getIsPersist, BaseConstants.DISABLED);
        baseOperationMapper.deleteByExample(queryWrapper);
    }

    @Override
    public void updateOpenStatusApis(int isOpen, List<Long> apiIds) {
        Weekend<BaseOperation> wrapper = Weekend.of(BaseOperation.class);
        wrapper.weekendCriteria()
                .andIn(BaseOperation::getApiId, apiIds);
        BaseOperation entity = new BaseOperation();
        entity.setIsOpen(isOpen);
        baseOperationMapper.updateByExampleSelective(entity, wrapper);
    }

    @Override
    public void updateStatusApis(int status, List<Long> apiIds) {
        Weekend<BaseOperation> queryWrapper = Weekend.of(BaseOperation.class);
        queryWrapper.weekendCriteria()
                .andIn(BaseOperation::getApiId, apiIds);
        BaseOperation entity = new BaseOperation();
        entity.setStatus(status);
        baseOperationMapper.updateByExampleSelective(entity, queryWrapper);
    }

    @Override
    public void updateAuthApis(int auth, List<Long> apiIds) {
        Weekend<BaseOperation> wrapper = Weekend.of(BaseOperation.class);
        wrapper.weekendCriteria()
                .andIn(BaseOperation::getApiId, apiIds)
                .andEqualTo(BaseOperation::getIsPersist, BaseConstants.DISABLED);
        BaseOperation entity = new BaseOperation();
        entity.setStatus(auth);
        baseOperationMapper.updateByExampleSelective(entity, wrapper);
    }
}