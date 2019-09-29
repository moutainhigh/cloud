package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.GatewayIpLimitService;
import com.smart4y.cloud.base.domain.model.GatewayIpLimit;
import com.smart4y.cloud.base.domain.model.GatewayIpLimitApi;
import com.smart4y.cloud.base.domain.repository.GatewayIpLimitApiCustomMapper;
import com.smart4y.cloud.base.domain.repository.GatewayIpLimitApiMapper;
import com.smart4y.cloud.base.domain.repository.GatewayIpLimitMapper;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtils;
import com.smart4y.cloud.core.interfaces.IpLimitApiDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class GatewayIpLimitServiceImpl implements GatewayIpLimitService {

    @Autowired
    private GatewayIpLimitMapper gatewayIpLimitMapper;
    @Autowired
    private GatewayIpLimitApiMapper gatewayIpLimitApiMapper;
    @Autowired
    private GatewayIpLimitApiCustomMapper gatewayIpLimitApiCustomMapper;

    @Override
    public PageInfo<GatewayIpLimit> findListPage(PageParams pageParams) {
        GatewayIpLimit query = pageParams.mapToObject(GatewayIpLimit.class);

        Weekend<GatewayIpLimit> queryWrapper = Weekend.of(GatewayIpLimit.class);
        WeekendCriteria<GatewayIpLimit, Object> criteria = queryWrapper.weekendCriteria();
        if (StringUtils.isNotBlank(query.getPolicyName())) {
            criteria.andLike(GatewayIpLimit::getPolicyName, query.getPolicyName() + "%");
        }
        if (null != query.getPolicyType()) {
            criteria.andEqualTo(GatewayIpLimit::getPolicyType, query.getPolicyType());
        }
        queryWrapper.orderBy("createdDate").desc();

        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), Boolean.TRUE);
        List<GatewayIpLimit> list = gatewayIpLimitMapper.selectByExample(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public List<IpLimitApiDTO> findBlackList() {
        return gatewayIpLimitApiCustomMapper.selectBlackList();
    }

    @Override
    public List<IpLimitApiDTO> findWhiteList() {
        return gatewayIpLimitApiCustomMapper.selectWhiteList();
    }

    @Override
    public List<GatewayIpLimitApi> findIpLimitApiList(long policyId) {
        Weekend<GatewayIpLimitApi> queryWrapper = Weekend.of(GatewayIpLimitApi.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(GatewayIpLimitApi::getPolicyId, policyId);
        return gatewayIpLimitApiMapper.selectByExample(queryWrapper);
    }

    @Override
    public GatewayIpLimit getIpLimitPolicy(long policyId) {
        return gatewayIpLimitMapper.selectByPrimaryKey(policyId);
    }

    @Override
    public GatewayIpLimit addIpLimitPolicy(GatewayIpLimit policy) {
        policy.setCreatedDate(LocalDateTime.now());
        policy.setLastModifiedDate(LocalDateTime.now());
        gatewayIpLimitMapper.insert(policy);
        return policy;
    }

    @Override
    public GatewayIpLimit updateIpLimitPolicy(GatewayIpLimit policy) {
        policy.setLastModifiedDate(LocalDateTime.now());
        gatewayIpLimitMapper.updateByPrimaryKeySelective(policy);
        return policy;
    }

    @Override
    public void removeIpLimitPolicy(long policyId) {
        clearIpLimitApisByPolicyId(policyId);
        gatewayIpLimitMapper.deleteByPrimaryKey(policyId);
    }

    @Override
    public void addIpLimitApis(long policyId, List<Long> apis) {
        // 先清空策略已有绑定
        clearIpLimitApisByPolicyId(policyId);
        for (Long apiId : apis) {
            // 先api解除所有绑定, 一个API只能绑定一个策略
            clearIpLimitApisByApiId(apiId);
            GatewayIpLimitApi item = new GatewayIpLimitApi();
            item.setApiId(apiId);
            item.setPolicyId(policyId);
            // 重新绑定策略
            gatewayIpLimitApiMapper.insert(item);

        }
    }

    @Override
    public void clearIpLimitApisByPolicyId(long policyId) {
        Weekend<GatewayIpLimitApi> queryWrapper = Weekend.of(GatewayIpLimitApi.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(GatewayIpLimitApi::getPolicyId, policyId);
        gatewayIpLimitApiMapper.deleteByExample(queryWrapper);
    }

    @Override
    public void clearIpLimitApisByApiId(long apiId) {
        Weekend<GatewayIpLimitApi> queryWrapper = Weekend.of(GatewayIpLimitApi.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(GatewayIpLimitApi::getApiId, apiId);
        gatewayIpLimitApiMapper.deleteByExample(queryWrapper);
    }
}