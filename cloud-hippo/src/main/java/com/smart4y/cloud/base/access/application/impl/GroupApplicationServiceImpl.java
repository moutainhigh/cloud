package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.GroupApplicationService;
import com.smart4y.cloud.base.access.domain.model.RbacGroup;
import com.smart4y.cloud.base.access.interfaces.dtos.group.RbacGroupPageQuery;
import com.smart4y.cloud.base.access.interfaces.dtos.group.RbacGroupQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

/**
 * @author Youtao
 * on 2020/7/31 15:49
 */
@Slf4j
@ApplicationService
public class GroupApplicationServiceImpl extends BaseDomainService<RbacGroup> implements GroupApplicationService {

    @Override
    public Page<RbacGroup> getGroupsPage(RbacGroupPageQuery query) {
        Weekend<RbacGroup> weekend = Weekend.of(RbacGroup.class);
        WeekendCriteria<RbacGroup, Object> criteria = weekend.weekendCriteria();
        if (null != query.getGroupParentId()) {
            criteria.andEqualTo(RbacGroup::getGroupParentId, query.getGroupParentId());
        }
        if (StringUtils.isNotBlank(query.getGroupType())) {
            criteria.andEqualTo(RbacGroup::getGroupType, query.getGroupType());
        }
        if (StringUtils.isNotBlank(query.getGroupState())) {
            criteria.andEqualTo(RbacGroup::getGroupState, query.getGroupState());
        }
        return this.findPage(weekend);
    }

    @Override
    public RbacGroup viewGroup(long groupId) {
        return this.getById(groupId);
    }

    @Override
    public List<RbacGroup> getGroups(RbacGroupQuery query) {
        Weekend<RbacGroup> weekend = Weekend.of(RbacGroup.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacGroup::getGroupParentId, query.getGroupId());
        return this.list(weekend);
    }
}