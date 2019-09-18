package com.smart4y.cloud.base.domain.service;

import com.smart4y.cloud.base.domain.model.BaseRole;
import com.smart4y.cloud.base.domain.repository.BaseCustomMapper;
import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/18.
 */
@DomainService
public class BaseRoleDomainService extends BaseDomainService<BaseRole> {

    private final BaseCustomMapper baseCustomMapper;

    @Autowired
    public BaseRoleDomainService(BaseCustomMapper baseCustomMapper) {
        this.baseCustomMapper = baseCustomMapper;
    }

    /**
     * 获取 用户角色
     */
    public List<BaseRole> getUserRoles(long userId) {
        return baseCustomMapper.selectUserRoles(userId);
    }
}