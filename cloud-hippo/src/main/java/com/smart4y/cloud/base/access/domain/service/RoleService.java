package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacRole;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Youtao
 * on 2020/8/4 16:16
 */
@DomainService
public class RoleService extends BaseDomainService<RbacRole> {

    /**
     * 超级管理员角色ID
     */
    public static final long ADMIN_ROLE_ID = 1L;

    /**
     * 获取指定角色列表
     *
     * @param roleIds 角色ID列表
     * @return 角色列表
     */
    public List<RbacRole> getByIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacRole> weekend = Weekend.of(RbacRole.class);
        weekend
                .weekendCriteria()
                .andIn(RbacRole::getRoleId, roleIds);
        weekend
                .orderBy("createdDate").desc();
        return this.list(weekend);
    }

    /**
     * 获取分页列表
     *
     * @param pageNo   页码
     * @param pageSize 页大小
     * @param roleName 名称
     * @param roleCode 标识
     * @return 分页列表
     */
    public Page<RbacRole> getPageLike(int pageNo, int pageSize, String roleName, String roleCode) {
        Weekend<RbacRole> weekend = Weekend.of(RbacRole.class);
        WeekendCriteria<RbacRole, Object> criteria = weekend.weekendCriteria();
        if (StringUtils.isNotBlank(roleName)) {
            criteria.andLike(RbacRole::getRoleName, "%" + roleName + "%");
        }
        if (StringUtils.isNotBlank(roleCode)) {
            criteria.andLike(RbacRole::getRoleCode, "%" + roleCode + "%");
        }
        weekend
                .orderBy("createdDate").desc();
        return this.findPage(weekend, pageNo, pageSize);
    }
}