package com.smart4y.cloud.base.domain.service;

import com.smart4y.cloud.base.domain.model.GatewayRateLimit;
import com.smart4y.cloud.base.domain.repository.GatewayCustomMapper;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@DomainService
public class GatewayRateLimitDomainService extends BaseDomainService<GatewayRateLimit> {

    private final GatewayCustomMapper gatewayCustomMapper;

    @Autowired
    public GatewayRateLimitDomainService(GatewayCustomMapper gatewayCustomMapper) {
        this.gatewayCustomMapper = gatewayCustomMapper;
    }

    /**
     * 获取 接口流量限制
     */
    public List<RateLimitApiDTO> getRateLimitApiList() {
        return gatewayCustomMapper.selectRateLimitApi();
    }
}