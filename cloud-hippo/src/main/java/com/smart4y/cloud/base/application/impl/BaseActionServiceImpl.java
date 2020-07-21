package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseActionService;
import com.smart4y.cloud.base.application.BaseAuthorityService;
import com.smart4y.cloud.base.domain.model.BaseAction;
import com.smart4y.cloud.base.infrastructure.mapper.BaseActionMapper;
import com.smart4y.cloud.base.interfaces.valueobject.query.BaseActionQuery;
import com.smart4y.cloud.core.application.ApplicationService;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.constants.ResourceType;
import com.smart4y.cloud.core.infrastructure.exception.OpenAlertException;
import com.smart4y.cloud.core.infrastructure.toolkit.base.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class BaseActionServiceImpl implements BaseActionService {

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    @Autowired
    private BaseActionMapper baseActionMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    @Override
    public PageInfo<BaseAction> findListPage(BaseActionQuery query) {
        Weekend<BaseAction> queryWrapper = Weekend.of(BaseAction.class);
        WeekendCriteria<BaseAction, Object> criteria = queryWrapper.weekendCriteria();
        if (StringHelper.isNotBlank(query.getActionCode())) {
            criteria.andLike(BaseAction::getActionCode, query.getActionCode() + "%");
        }
        if (StringHelper.isNotBlank(query.getActionName())) {
            criteria.andLike(BaseAction::getActionName, query.getActionName() + "%");
        }
        queryWrapper.orderBy("createdDate").desc();

        PageHelper.startPage(query.getPage(), query.getLimit(), Boolean.TRUE);
        List<BaseAction> list = baseActionMapper.selectByExample(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public List<BaseAction> findListByMenuId(Long menuId) {
        Weekend<BaseAction> queryWrapper = Weekend.of(BaseAction.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseAction::getMenuId, menuId);
        List<BaseAction> list = baseActionMapper.selectByExample(queryWrapper);
        //根据优先级从小到大排序
        list.sort(Comparator.comparing(BaseAction::getPriority));
        return list;
    }

    @Override
    public BaseAction getAction(long actionId) {
        return baseActionMapper.selectByPrimaryKey(actionId);
    }

    @Override
    public Boolean isExist(String actionCode) {
        Weekend<BaseAction> queryWrapper = Weekend.of(BaseAction.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseAction::getActionCode, actionCode);
        int count = baseActionMapper.selectCountByExample(queryWrapper);
        return count > 0;
    }

    @Override
    public BaseAction addAction(BaseAction aciton) {
        if (isExist(aciton.getActionCode())) {
            throw new OpenAlertException(String.format("%s编码已存在!", aciton.getActionCode()));
        }
        if (aciton.getMenuId() == null) {
            aciton.setMenuId(0L);
        }
        if (aciton.getPriority() == null) {
            aciton.setPriority(0);
        }
        if (aciton.getStatus() == null) {
            aciton.setStatus(BaseConstants.ENABLED);
        }
        if (aciton.getIsPersist() == null) {
            aciton.setIsPersist(BaseConstants.DISABLED);
        }
        aciton.setCreatedDate(LocalDateTime.now());
        aciton.setServiceId(DEFAULT_SERVICE_ID);
        aciton.setLastModifiedDate(LocalDateTime.now());
        baseActionMapper.insert(aciton);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(aciton.getActionId(), ResourceType.action);
        return aciton;
    }

    @Override
    public BaseAction updateAction(BaseAction aciton) {
        BaseAction saved = getAction(aciton.getActionId());
        if (saved == null) {
            throw new OpenAlertException(String.format("%s信息不存在", aciton.getActionId()));
        }
        if (!saved.getActionCode().equals(aciton.getActionCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(aciton.getActionCode())) {
                throw new OpenAlertException(String.format("%s编码已存在!", aciton.getActionCode()));
            }
        }
        if (aciton.getMenuId() == null) {
            aciton.setMenuId(0L);
        }
        if (aciton.getPriority() == null) {
            aciton.setPriority(0);
        }
        aciton.setLastModifiedDate(LocalDateTime.now());
        baseActionMapper.updateByPrimaryKeySelective(aciton);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(aciton.getActionId(), ResourceType.action);
        return aciton;
    }

    @Override
    public void removeAction(Long actionId) {
        BaseAction aciton = getAction(actionId);
        if (aciton != null && aciton.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException("保留数据，不允许删除");
        }
        baseAuthorityService.removeAuthorityAction(actionId);
        baseAuthorityService.removeAuthority(actionId, ResourceType.action);
        baseActionMapper.deleteByPrimaryKey(actionId);
    }

    @Override
    public void removeByMenuId(Long menuId) {
        List<BaseAction> actionList = findListByMenuId(menuId);
        if (actionList != null && actionList.size() > 0) {
            for (BaseAction action : actionList) {
                removeAction(action.getActionId());
            }
        }
    }
}