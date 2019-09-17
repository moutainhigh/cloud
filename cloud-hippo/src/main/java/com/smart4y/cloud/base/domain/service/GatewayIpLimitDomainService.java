package com.smart4y.cloud.base.domain.service;

import com.smart4y.cloud.base.domain.model.GatewayIpLimit;
import com.smart4y.cloud.base.domain.repository.GatewayCustomMapper;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@DomainService
public class GatewayIpLimitDomainService extends BaseDomainService<GatewayIpLimit> {

    private final GatewayCustomMapper gatewayCustomMapper;

    @Autowired
    public GatewayIpLimitDomainService(GatewayCustomMapper gatewayCustomMapper) {
        this.gatewayCustomMapper = gatewayCustomMapper;
    }

    /**
     * 查询黑名单或白名单
     *
     * @param policyType 0黑名单 1白名单
     */
    public List<IpLimitApiDTO> getBlackOrWhiteList(int policyType) {
        return gatewayCustomMapper.selectIpLimitApi(policyType);
    }
}