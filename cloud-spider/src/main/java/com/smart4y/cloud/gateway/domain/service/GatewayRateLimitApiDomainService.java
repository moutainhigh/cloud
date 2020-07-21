package com.smart4y.cloud.gateway.domain.service;

import com.smart4y.cloud.core.domain.DomainService;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import com.smart4y.cloud.gateway.domain.RateLimitApiObj;
import com.smart4y.cloud.gateway.domain.model.GatewayRateLimitApi;
import com.smart4y.cloud.gateway.infrastructure.mapper.GatewayCustomMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Slf4j
@DomainService
public class GatewayRateLimitApiDomainService extends BaseDomainService<GatewayRateLimitApi> {

    private final GatewayCustomMapper gatewayCustomMapper;

    @Autowired
    public GatewayRateLimitApiDomainService(GatewayCustomMapper gatewayCustomMapper) {
        this.gatewayCustomMapper = gatewayCustomMapper;
    }

    /**
     * 查询 路由限流数据
     *
     * @return 路由限流数据
     */
    public List<RateLimitApiObj> findRateLimitApis() {
        return gatewayCustomMapper.findRateLimitApis();
    }
}