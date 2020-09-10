package com.smart4y.cloud.hippo.domain.service;

import com.smart4y.cloud.hippo.domain.entity.*;
import com.smart4y.cloud.hippo.infrastructure.persistence.mybatis.RbacPrivilegeElementMapper;
import com.smart4y.cloud.hippo.infrastructure.persistence.mybatis.RbacPrivilegeMenuMapper;
import com.smart4y.cloud.hippo.infrastructure.persistence.mybatis.RbacPrivilegeOperationMapper;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public PrivilegeService(RbacPrivilegeElementMapper rbacPrivilegeElementMapper, RbacPrivilegeOperationMapper rbacPrivilegeOperationMapper, RbacPrivilegeMenuMapper rbacPrivilegeMenuMapper) {
        this.rbacPrivilegeElementMapper = rbacPrivilegeElementMapper;
        this.rbacPrivilegeOperationMapper = rbacPrivilegeOperationMapper;
        this.rbacPrivilegeMenuMapper = rbacPrivilegeMenuMapper;
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
     * 获取指定元素的权限列表
     *
     * @param elementIds 元素ID列表
     * @return 权限列表
     */
    public List<RbacPrivilegeElement> getPrivilegeElementsByElementIds(Collection<Long> elementIds) {
        if (CollectionUtils.isEmpty(elementIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilegeElement> weekend = Weekend.of(RbacPrivilegeElement.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilegeElement::getElementId, elementIds);
        weekend
                .orderBy("createdDate").desc();
        return rbacPrivilegeElementMapper.selectByExample(weekend);
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
     * 获取指定菜单权限
     *
     * @param menuId 菜单ID
     * @return 菜单权限
     */
    public Optional<RbacPrivilegeMenu> getPrivilegeMenuByMenuId(long menuId) {
        Weekend<RbacPrivilegeMenu> weekend = Weekend.of(RbacPrivilegeMenu.class);
        weekend
                .weekendCriteria()
                .andEqualTo(RbacPrivilegeMenu::getMenuId, menuId);
        return Optional.ofNullable(rbacPrivilegeMenuMapper.selectOneByExample(weekend));
    }

    /**
     * 获取指定菜单权限列表
     *
     * @param privilegeIds 权限ID列表
     * @return 菜单权限列表
     */
    public List<RbacPrivilegeMenu> getPrivilegeMenusByPrivilegeIds(Collection<Long> privilegeIds) {
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return Collections.emptyList();
        }
        Weekend<RbacPrivilegeMenu> weekend = Weekend.of(RbacPrivilegeMenu.class);
        weekend
                .weekendCriteria()
                .andIn(RbacPrivilegeMenu::getPrivilegeId, privilegeIds);
        return rbacPrivilegeMenuMapper.selectByExample(weekend);
    }

    /**
     * 添加菜单权限
     *
     * @param menuId   菜单ID
     * @param menuCode 菜单标识
     */
    public void savePrivilegeMenu(long menuId, String menuCode) {
        RbacPrivilege privilege = new RbacPrivilege()
                .setPrivilege(menuCode)
                .setPrivilegeType("m")
                .setCreatedDate(LocalDateTime.now());
        this.save(privilege);

        RbacPrivilegeMenu privilegeMenu = new RbacPrivilegeMenu()
                .setPrivilegeId(privilege.getPrivilegeId())
                .setMenuId(menuId)
                .setCreatedDate(LocalDateTime.now());
        rbacPrivilegeMenuMapper.insertSelective(privilegeMenu);
    }

    /**
     * 添加元素权限
     *
     * @param elementId   元素ID
     * @param elementCode 元素标识
     */
    public void savePrivilegeElement(long elementId, String elementCode) {
        RbacPrivilege privilege = new RbacPrivilege()
                .setPrivilege(elementCode)
                .setPrivilegeType("e")
                .setCreatedDate(LocalDateTime.now());
        this.save(privilege);

        RbacPrivilegeElement privilegeElement = new RbacPrivilegeElement()
                .setPrivilegeId(privilege.getPrivilegeId())
                .setElementId(elementId)
                .setCreatedDate(LocalDateTime.now());
        rbacPrivilegeElementMapper.insertSelective(privilegeElement);
    }

    /**
     * 添加操作权限
     *
     * @param operations 操作列表
     */
    public void savePrivilegeOperation(Collection<RbacOperation> operations) {
        if (CollectionUtils.isEmpty(operations)) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        List<RbacPrivilege> newPrivileges = operations.stream()
                .map(o -> new RbacPrivilege()
                        .setPrivilege(o.getOperationCode())
                        .setPrivilegeType("o")
                        .setCreatedDate(now))
                .collect(Collectors.toList());
        this.saveBatch(newPrivileges);

        Map<String, Long> operationIdCodeMap = operations.stream()
                .collect(Collectors.toMap(RbacOperation::getOperationCode, RbacOperation::getOperationId));
        List<RbacPrivilegeOperation> newPrivilegeOperations = newPrivileges.stream()
                .map(privilege -> new RbacPrivilegeOperation()
                        .setPrivilegeId(privilege.getPrivilegeId())
                        .setOperationId(operationIdCodeMap.get(privilege.getPrivilege()))
                        .setCreatedDate(now))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(newPrivilegeOperations)) {
            rbacPrivilegeOperationMapper.insertList(newPrivilegeOperations);
        }
    }

    /**
     * 移除权限
     *
     * @param privilegeType 权限类型
     * @param privilegeIds  权限ID列表
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