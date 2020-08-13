package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacUser;
import com.smart4y.cloud.base.access.domain.entity.RbacUserRole;
import com.smart4y.cloud.base.access.infrastructure.persistence.mybatis.RbacUserRoleMapper;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserService extends BaseDomainService<RbacUser> {

    @Autowired
    private RbacUserRoleMapper rbacUserRoleMapper;

    /**
     * 获取指定用户列表
     *
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    public List<RbacUser> getByIds(Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacUser> weekend = Weekend.of(RbacUser.class);
        weekend
                .weekendCriteria()
                .andIn(RbacUser::getUserId, userIds);
        return this.list(weekend);
    }

    /**
     * 获取分页列表
     *
     * @param pageNo   页码
     * @param pageSize 页大小
     * @param userId   用户ID
     * @param userName 用户名
     * @return 分页列表
     */
    public Page<RbacUser> getPageLike(int pageNo, int pageSize, Long userId, String userName) {
        Weekend<RbacUser> weekend = Weekend.of(RbacUser.class);
        WeekendCriteria<RbacUser, Object> criteria = weekend.weekendCriteria();
        if (null != userId) {
            criteria.andLike(RbacUser::getUserId, "%" + userId + "%");
        }
        if (StringUtils.isNotBlank(userName)) {
            criteria.andLike(RbacUser::getUserName, "%" + userName + "%");
        }
        weekend
                .orderBy("createdDate").desc();
        return this.findPage(weekend, pageNo, pageSize);
    }

    /**
     * 获取指定用户的角色关联列表
     *
     * @param userId 用户ID
     * @return 角色关联列表
     */
    public List<RbacUserRole> getUserRolesByUserId(long userId) {
        Weekend<RbacUserRole> weekend = Weekend.of(RbacUserRole.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacUserRole::getUserId, userId);
        return rbacUserRoleMapper.selectByExample(weekend);
    }
}