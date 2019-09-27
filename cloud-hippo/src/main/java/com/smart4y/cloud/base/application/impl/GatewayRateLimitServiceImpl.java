package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.GatewayRateLimitService;
import com.smart4y.cloud.base.domain.model.GatewayRateLimit;
import com.smart4y.cloud.base.domain.model.GatewayRateLimitApi;
import com.smart4y.cloud.base.domain.repository.GatewayRateLimitApiCustomMapper;
import com.smart4y.cloud.base.domain.repository.GatewayRateLimitApiMapper;
import com.smart4y.cloud.base.domain.repository.GatewayRateLimitMapper;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.model.Page;
import com.smart4y.cloud.core.domain.PageParams;
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
public class GatewayRateLimitServiceImpl implements GatewayRateLimitService {

    @Autowired
    private GatewayRateLimitMapper gatewayRateLimitMapper;
    @Autowired
    private GatewayRateLimitApiMapper gatewayRateLimitApiMapper;
    @Autowired
    private GatewayRateLimitApiCustomMapper gatewayRateLimitApiCustomMapper;

    @Override
    public IPage<GatewayRateLimit> findListPage(PageParams pageParams) {
        GatewayRateLimit query = pageParams.mapToObject(GatewayRateLimit.class);

        Weekend<GatewayRateLimit> queryWrapper = Weekend.of(GatewayRateLimit.class);
        WeekendCriteria<GatewayRateLimit, Object> criteria = queryWrapper.weekendCriteria();
        if (StringUtils.isNotBlank(query.getPolicyName())) {
            criteria.andLike(GatewayRateLimit::getPolicyName, query.getPolicyName() + "%");
        }
        if (null != query.getPolicyType()) {
            criteria.andEqualTo(GatewayRateLimit::getPolicyType, query.getPolicyType());
        }
        queryWrapper.orderBy("createdDate").desc();

        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), Boolean.TRUE);
        List<GatewayRateLimit> list = gatewayRateLimitMapper.selectByExample(queryWrapper);
        PageInfo<GatewayRateLimit> pageInfo = new PageInfo<>(list);
        IPage<GatewayRateLimit> page = new Page<>();
        page.setRecords(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }

    @Override
    public List<RateLimitApiDTO> findRateLimitApiList() {
        return gatewayRateLimitApiCustomMapper.selectRateLimitApi();
    }

    @Override
    public List<GatewayRateLimitApi> findRateLimitApiList(Long policyId) {
        Weekend<GatewayRateLimitApi> queryWrapper = Weekend.of(GatewayRateLimitApi.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(GatewayRateLimitApi::getPolicyId, policyId);
        return gatewayRateLimitApiMapper.selectByExample(queryWrapper);
    }

    @Override
    public GatewayRateLimit getRateLimitPolicy(Long policyId) {
        return gatewayRateLimitMapper.selectByPrimaryKey(policyId);
    }

    @Override
    public GatewayRateLimit addRateLimitPolicy(GatewayRateLimit policy) {
        policy.setCreatedDate(LocalDateTime.now());
        policy.setLastModifiedDate(LocalDateTime.now());
        gatewayRateLimitMapper.insertSelective(policy);
        return policy;
    }

    @Override
    public GatewayRateLimit updateRateLimitPolicy(GatewayRateLimit policy) {
        policy.setLastModifiedDate(LocalDateTime.now());
        gatewayRateLimitMapper.updateByPrimaryKeySelective(policy);
        return policy;
    }

    @Override
    public void removeRateLimitPolicy(Long policyId) {
        clearRateLimitApisByPolicyId(policyId);
        gatewayRateLimitMapper.deleteByPrimaryKey(policyId);
    }

    @Override
    public void addRateLimitApis(Long policyId, String... apis) {
        // 先清空策略已有绑定
        clearRateLimitApisByPolicyId(policyId);
        if (apis != null && apis.length > 0) {
            for (String api : apis) {
                Long apiId = Long.parseLong(api);
                // 先api解除所有绑定, 一个API只能绑定一个策略
                clearRateLimitApisByApiId(apiId);
                GatewayRateLimitApi item = new GatewayRateLimitApi();
                item.setApiId(apiId);
                item.setPolicyId(policyId);
                // 重新绑定策略
                gatewayRateLimitApiMapper.insertSelective(item);
            }
        }
    }

    @Override
    public void clearRateLimitApisByPolicyId(Long policyId) {
        Weekend<GatewayRateLimitApi> queryWrapper = Weekend.of(GatewayRateLimitApi.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(GatewayRateLimitApi::getPolicyId, policyId);
        gatewayRateLimitApiMapper.deleteByExample(queryWrapper);
    }

    @Override
    public void clearRateLimitApisByApiId(Long apiId) {
        Weekend<GatewayRateLimitApi> queryWrapper = Weekend.of(GatewayRateLimitApi.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(GatewayRateLimitApi::getApiId, apiId);
        gatewayRateLimitApiMapper.deleteByExample(queryWrapper);
    }
}