package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseRoleService;
import com.smart4y.cloud.base.application.BaseUserService;
import com.smart4y.cloud.base.domain.model.BaseRole;
import com.smart4y.cloud.base.domain.model.BaseRoleUser;
import com.smart4y.cloud.base.domain.model.BaseUser;
import com.smart4y.cloud.base.domain.repository.BaseRoleMapper;
import com.smart4y.cloud.base.domain.repository.BaseRoleUserCustomMapper;
import com.smart4y.cloud.base.domain.repository.BaseRoleUserMapper;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.Page;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.constants.CommonConstants;
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
public class BaseRoleServiceImpl implements BaseRoleService {

    @Autowired
    private BaseRoleMapper baseRoleMapper;
    @Autowired
    private BaseRoleUserMapper baseRoleUserMapper;
    @Autowired
    private BaseRoleUserCustomMapper baseRoleUserCustomMapper;
    @Autowired
    private BaseUserService baseUserService;

    @Override
    public IPage<BaseRole> findListPage(PageParams pageParams) {
        BaseRole query = pageParams.mapToObject(BaseRole.class);

        Weekend<BaseRole> queryWrapper = Weekend.of(BaseRole.class);
        WeekendCriteria<BaseRole, Object> criteria = queryWrapper.weekendCriteria();
        if (StringUtils.isNotBlank(query.getRoleCode())) {
            criteria.andLike(BaseRole::getRoleCode, query.getRoleCode() + "%");
        }
        if (StringUtils.isNotBlank(query.getRoleName())) {
            criteria.andLike(BaseRole::getRoleName, query.getRoleName() + "%");
        }
        queryWrapper.orderBy("createdDate").desc();

        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), Boolean.TRUE);
        List<BaseRole> list = baseRoleMapper.selectByExample(queryWrapper);
        PageInfo<BaseRole> pageInfo = new PageInfo<>(list);
        IPage<BaseRole> page = new Page<>();
        page.setRecords(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }

    @Override
    public List<BaseRole> findAllList() {
        return baseRoleMapper.selectAll();
    }

    @Override
    public BaseRole getRole(long roleId) {
        return baseRoleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public BaseRole addRole(BaseRole role) {
        if (isExist(role.getRoleCode())) {
            throw new OpenAlertException(String.format("%s编码已存在!", role.getRoleCode()));
        }
        if (role.getStatus() == null) {
            role.setStatus(BaseConstants.ENABLED);
        }
        if (role.getIsPersist() == null) {
            role.setIsPersist(BaseConstants.DISABLED);
        }
        role.setCreatedDate(LocalDateTime.now());
        role.setLastModifiedDate(LocalDateTime.now());
        baseRoleMapper.insertSelective(role);
        return role;
    }

    @Override
    public BaseRole updateRole(BaseRole role) {
        BaseRole saved = getRole(role.getRoleId());
        if (saved == null) {
            throw new OpenAlertException("信息不存在!");
        }
        if (!saved.getRoleCode().equals(role.getRoleCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(role.getRoleCode())) {
                throw new OpenAlertException(String.format("%s编码已存在!", role.getRoleCode()));
            }
        }
        role.setLastModifiedDate(LocalDateTime.now());
        baseRoleMapper.updateByPrimaryKeySelective(role);
        return role;
    }

    @Override
    public void removeRole(long roleId) {
        BaseRole role = getRole(roleId);
        if (role != null && role.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException("保留数据，不允许删除");
        }
        int count = getCountByRole(roleId);
        if (count > 0) {
            throw new OpenAlertException("该角色下存在授权人员,不允许删除!");
        }
        baseRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public Boolean isExist(String roleCode) {
        if (StringUtils.isBlank(roleCode)) {
            throw new OpenAlertException("roleCode不能为空!");
        }
        Weekend<BaseRole> queryWrapper = Weekend.of(BaseRole.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseRole::getRoleCode, roleCode);
        return baseRoleMapper.selectCountByExample(queryWrapper) > 0;
    }

    @Override
    public void saveUserRoles(long userId, List<Long> roles) {
        if (roles == null) {
            return;
        }
        BaseUser user = baseUserService.getUserById(userId);
        if (user == null) {
            return;
        }
        if (CommonConstants.ROOT.equals(user.getUserName())) {
            throw new OpenAlertException("默认用户无需分配!");
        }
        // 先清空,在添加
        removeUserRoles(userId);
        for (Long roleId : roles) {
            BaseRoleUser roleUser = new BaseRoleUser();
            roleUser.setUserId(userId);
            roleUser.setRoleId(roleId);
            baseRoleUserMapper.insert(roleUser);
        }
        // 批量保存
    }

    @Override
    public void saveRoleUsers(long roleId, List<Long> userIds) {
        if (userIds == null) {
            return;
        }
        // 先清空,在添加
        removeRoleUsers(roleId);
        for (Long userId : userIds) {
            BaseRoleUser roleUser = new BaseRoleUser();
            roleUser.setUserId(userId);
            roleUser.setRoleId(roleId);
            baseRoleUserMapper.insert(roleUser);
        }
        // 批量保存
    }


    @Override
    public List<BaseRoleUser> findRoleUsers(long roleId) {
        Weekend<BaseRoleUser> queryWrapper = Weekend.of(BaseRoleUser.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseRoleUser::getRoleId, roleId);
        return baseRoleUserMapper.selectByExample(queryWrapper);
    }


    @Override
    public int getCountByRole(long roleId) {
        Weekend<BaseRoleUser> queryWrapper = Weekend.of(BaseRoleUser.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseRoleUser::getRoleId, roleId);
        return baseRoleUserMapper.selectCountByExample(queryWrapper);
    }

    @Override
    public int getCountByUser(long userId) {
        Weekend<BaseRoleUser> queryWrapper = Weekend.of(BaseRoleUser.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseRoleUser::getUserId, userId);
        return baseRoleUserMapper.selectCountByExample(queryWrapper);
    }

    @Override
    public void removeRoleUsers(long roleId) {
        Weekend<BaseRoleUser> queryWrapper = Weekend.of(BaseRoleUser.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseRoleUser::getRoleId, roleId);
        baseRoleUserMapper.deleteByExample(queryWrapper);
    }

    @Override
    public void removeUserRoles(long userId) {
        Weekend<BaseRoleUser> queryWrapper = Weekend.of(BaseRoleUser.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseRoleUser::getUserId, userId);
        baseRoleUserMapper.deleteByExample(queryWrapper);
    }

    @Override
    public Boolean isExist(long userId, long roleId) {
        Weekend<BaseRoleUser> queryWrapper = Weekend.of(BaseRoleUser.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseRoleUser::getRoleId, roleId)
                .andEqualTo(BaseRoleUser::getUserId, userId);
        baseRoleUserMapper.deleteByExample(queryWrapper);
        int result = baseRoleUserMapper.selectCountByExample(queryWrapper);
        return result > 0;
    }

    @Override
    public List<BaseRole> getUserRoles(long userId) {
        return baseRoleUserCustomMapper.selectRoleUserList(userId);
    }

    @Override
    public List<Long> getUserRoleIds(long userId) {
        return baseRoleUserCustomMapper.selectRoleUserIdList(userId);
    }
}