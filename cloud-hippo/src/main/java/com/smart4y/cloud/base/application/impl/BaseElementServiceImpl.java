package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseElementService;
import com.smart4y.cloud.base.application.BaseAuthorityService;
import com.smart4y.cloud.base.domain.model.BaseElement;
import com.smart4y.cloud.base.infrastructure.mapper.BaseElementMapper;
import com.smart4y.cloud.base.interfaces.query.BaseActionQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.constant.BaseConstants;
import com.smart4y.cloud.core.constant.ResourceType;
import com.smart4y.cloud.core.exception.OpenAlertException;
import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.toolkit.base.StringHelper;
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
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class BaseElementServiceImpl implements BaseElementService {

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    @Autowired
    private BaseElementMapper baseElementMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    @Override
    public PageInfo<BaseElement> findListPage(BaseActionQuery query) {
        Weekend<BaseElement> queryWrapper = Weekend.of(BaseElement.class);
        WeekendCriteria<BaseElement, Object> criteria = queryWrapper.weekendCriteria();
        if (StringHelper.isNotBlank(query.getActionCode())) {
            criteria.andLike(BaseElement::getActionCode, query.getActionCode() + "%");
        }
        if (StringHelper.isNotBlank(query.getActionName())) {
            criteria.andLike(BaseElement::getActionName, query.getActionName() + "%");
        }
        queryWrapper.orderBy("createdDate").desc();

        PageHelper.startPage(query.getPage(), query.getLimit(), Boolean.TRUE);
        List<BaseElement> list = baseElementMapper.selectByExample(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public List<BaseElement> findListByMenuId(Long menuId) {
        Weekend<BaseElement> queryWrapper = Weekend.of(BaseElement.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseElement::getMenuId, menuId);
        List<BaseElement> list = baseElementMapper.selectByExample(queryWrapper);
        //根据优先级从小到大排序
        list.sort(Comparator.comparing(BaseElement::getPriority));
        return list;
    }

    @Override
    public BaseElement getAction(long actionId) {
        return baseElementMapper.selectByPrimaryKey(actionId);
    }

    @Override
    public Boolean isExist(String actionCode) {
        Weekend<BaseElement> queryWrapper = Weekend.of(BaseElement.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseElement::getActionCode, actionCode);
        int count = baseElementMapper.selectCountByExample(queryWrapper);
        return count > 0;
    }

    @Override
    public BaseElement addAction(BaseElement aciton) {
        if (isExist(aciton.getActionCode())) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, String.format("%s编码已存在!", aciton.getActionCode()));
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
        baseElementMapper.insert(aciton);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(aciton.getActionId(), ResourceType.action);
        return aciton;
    }

    @Override
    public BaseElement updateAction(BaseElement aciton) {
        BaseElement saved = getAction(aciton.getActionId());
        if (saved == null) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, String.format("%s信息不存在", aciton.getActionId()));
        }
        if (!saved.getActionCode().equals(aciton.getActionCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(aciton.getActionCode())) {
                throw new OpenAlertException(MessageType.BAD_REQUEST, String.format("%s编码已存在!", aciton.getActionCode()));
            }
        }
        if (aciton.getMenuId() == null) {
            aciton.setMenuId(0L);
        }
        if (aciton.getPriority() == null) {
            aciton.setPriority(0);
        }
        aciton.setLastModifiedDate(LocalDateTime.now());
        baseElementMapper.updateByPrimaryKeySelective(aciton);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(aciton.getActionId(), ResourceType.action);
        return aciton;
    }

    @Override
    public void removeAction(Long actionId) {
        BaseElement aciton = getAction(actionId);
        if (aciton != null && aciton.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "保留数据，不允许删除");
        }
        baseAuthorityService.removeAuthorityAction(actionId);
        baseAuthorityService.removeAuthority(actionId, ResourceType.action);
        baseElementMapper.deleteByPrimaryKey(actionId);
    }

    @Override
    public void removeByMenuId(Long menuId) {
        List<BaseElement> actionList = findListByMenuId(menuId);
        if (actionList != null && actionList.size() > 0) {
            for (BaseElement action : actionList) {
                removeAction(action.getActionId());
            }
        }
    }
}