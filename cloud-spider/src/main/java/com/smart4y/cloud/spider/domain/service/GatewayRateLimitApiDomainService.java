package com.smart4y.cloud.spider.domain.service;

import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.spider.domain.RateLimitApiObj;
import com.smart4y.cloud.spider.domain.model.GatewayRateLimitApi;
import com.smart4y.cloud.mapper.BaseDomainService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * @author Youtao
 * Created by youtao on 2019/9/17.
 */
@Slf4j
@DomainService
public class GatewayRateLimitApiDomainService extends BaseDomainService<GatewayRateLimitApi> {

    /**
     * TODO 查询 路由限流数据
     *
     * @return 路由限流数据
     */
    public List<RateLimitApiObj> findRateLimitApis() {
//        return gatewayCustomMapper.findRateLimitApis();
        return Collections.emptyList();
    }
}