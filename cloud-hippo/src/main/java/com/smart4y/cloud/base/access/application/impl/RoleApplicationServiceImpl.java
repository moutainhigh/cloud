package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.RoleApplicationService;
import com.smart4y.cloud.base.access.domain.model.RbacRole;
import com.smart4y.cloud.base.access.domain.service.RoleService;
import com.smart4y.cloud.base.access.interfaces.dtos.role.RbacRolePageQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.message.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 * @author Youtao on 2020/8/10 16:45
 */
@Slf4j
@ApplicationService
public class RoleApplicationServiceImpl implements RoleApplicationService {

    @Autowired
    private RoleService roleService;

    @Override
    public Page<RbacRole> getRolesPage(RbacRolePageQuery query) {
        Weekend<RbacRole> weekend = Weekend.of(RbacRole.class);
        WeekendCriteria<RbacRole, Object> criteria = weekend.weekendCriteria();
        if (StringUtils.isNotBlank(query.getRoleName())) {
            criteria.andLike(RbacRole::getRoleName, "%" + query.getRoleName() + "%");
        }
        if (StringUtils.isNotBlank(query.getRoleCode())) {
            criteria.andLike(RbacRole::getRoleCode, "%" + query.getRoleCode() + "%");
        }
        weekend
                .orderBy("createdDate").desc();

        return roleService.findPage(weekend, query.getPage(), query.getLimit());
    }
}