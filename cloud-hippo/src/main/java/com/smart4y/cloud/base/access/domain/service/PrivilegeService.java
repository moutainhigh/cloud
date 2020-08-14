package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.RbacPrivilege;
import com.smart4y.cloud.base.access.domain.entity.RbacPrivilegeElement;
import com.smart4y.cloud.base.access.domain.entity.RbacPrivilegeMenu;
import com.smart4y.cloud.base.access.domain.entity.RbacPrivilegeOperation;
import com.smart4y.cloud.base.access.infrastructure.persistence.mybatis.RbacPrivilegeElementMapper;
import com.smart4y.cloud.base.access.infrastructure.persistence.mybatis.RbacPrivilegeMenuMapper;
import com.smart4y.cloud.base.access.infrastructure.persistence.mybatis.RbacPrivilegeOperationMapper;
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
 * on 2020/8/7 14:51
 */
@DomainService
public class PrivilegeService extends BaseDomainService<RbacPrivilege> {

    private final RbacPrivilegeElementMapper rbacPrivilegeElementMapper;
    private final RbacPrivilegeOperationMapper rbacPrivilegeOperationMapper;
    private final RbacPrivilegeMenuMapper rbacPrivilegeMenuMapper;

    @Autowired
    public PrivilegeService(RbacPrivilegeOperationMapper rbacPrivilegeOperationMapper, RbacPrivilegeMenuMapper rbacPrivilegeMenuMapper, RbacPrivilegeElementMapper rbacPrivilegeElementMapper) {
        this.rbacPrivilegeOperationMapper = rbacPrivilegeOperationMapper;
        this.rbacPrivilegeMenuMapper = rbacPrivilegeMenuMapper;
        this.rbacPrivilegeElementMapper = rbacPrivilegeElementMapper;
    }

    /**
     * 获取分页列表
     *
     * @param pageNo        页码
     * @param pageSize      页大小
     * @param privilegeId   ID
     * @param privilege     标识
     * @param privilegeType 类型
     * @return 分页列表
     */
    public Page<RbacPrivilege> getPageLike(int pageNo, int pageSize, Long privilegeId, String privilege, String privilegeType) {
        Weekend<RbacPrivilege> weekend = Weekend.of(RbacPrivilege.class);
        WeekendCriteria<RbacPrivilege, Object> criteria = weekend.weekendCriteria();
        if (null != privilegeId) {
            criteria.andLike(RbacPrivilege::getPrivilegeId, "%" + privilegeId + "%");
        }
        if (StringUtils.isNotBlank(privilege)) {
            criteria.andLike(RbacPrivilege::getPrivilege, "%" + privilege + "%");
        }
        if (StringUtils.isNotBlank(privilegeType)) {
            criteria.andEqualTo(RbacPrivilege::getPrivilegeType, privilegeType);
        }
        weekend
                .orderBy("createdDate").desc();
        return this.findPage(weekend, pageNo, pageSize);
    }

    /**
     * 获取指定操作的权限列表
     *
     * @param operationIds 操作ID列表
     * @return 权限列表
     */
    public List<RbacPrivilegeOperation> getPrivilegeOperationsByOperationIds(Collection<Long> operationIds) {
        if (CollectionUtils.isEmpty(operationIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilegeOperation> weekend = Weekend.of(RbacPrivilegeOperation.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilegeOperation::getOperationId, operationIds);
        weekend
                .orderBy("createdDate").desc();
        return rbacPrivilegeOperationMapper.selectByExample(weekend);
    }

    /**
     * 获取指定的权限列表
     *
     * @param privilegeIds 权限ID列表
     * @return 权限列表
     */
    public List<RbacPrivilege> getByIds(Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilege> weekend = Weekend.of(RbacPrivilege.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilege::getPrivilegeId, privilegeIds);
        weekend
                .orderBy("createdDate").desc();
        return this.list(weekend);
    }

    /**
     * 获取指定的权限列表
     *
     * @param privilegeType 权限类别
     * @param privileges    权限标识列表
     * @return 权限列表
     */
    public List<RbacPrivilege> getByType(String privilegeType, Collection<String> privileges) {
        if (CollectionUtils.isEmpty(privileges)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilege> weekend = Weekend.of(RbacPrivilege.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacPrivilege::getPrivilegeType, privilegeType)
                .andIn(RbacPrivilege::getPrivilege, privileges);
        weekend
                .orderBy("createdDate").desc();
        return this.list(weekend);
    }

    /**
     * 移除权限
     */
    public void removeByPrivilege(String privilegeType, Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return;
        }
        Weekend<RbacPrivilege> weekend = Weekend.of(RbacPrivilege.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilege::getPrivilegeId, privilegeIds);
        this.remove(weekend);

        switch (privilegeType) {
            case "m":
                Weekend<RbacPrivilegeMenu> menuWeekend = Weekend.of(RbacPrivilegeMenu.class);
                menuWeekend
                        .weekendCriteria()
                        .andIn(RbacPrivilegeMenu::getPrivilegeId, privilegeIds);
                rbacPrivilegeMenuMapper.deleteByExample(menuWeekend);
                break;
            case "o":
                Weekend<RbacPrivilegeOperation> operationWeekend = Weekend.of(RbacPrivilegeOperation.class);
                operationWeekend
                        .weekendCriteria()
                        .andIn(RbacPrivilegeOperation::getPrivilegeId, privilegeIds);
                rbacPrivilegeOperationMapper.deleteByExample(operationWeekend);
                break;
            case "e":
                Weekend<RbacPrivilegeElement> elementWeekend = Weekend.of(RbacPrivilegeElement.class);
                elementWeekend
                        .weekendCriteria()
                        .andIn(RbacPrivilegeElement::getPrivilegeId, privilegeIds);
                rbacPrivilegeElementMapper.deleteByExample(elementWeekend);
                break;
        }
    }
}