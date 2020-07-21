package com.smart4y.cloud.gateway.infrastructure.mapper;

import com.smart4y.cloud.gateway.domain.RateLimitApiObj;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Mapper
@Repository
public interface GatewayCustomMapper {

    /**
     * 查询路由限流数据
     */
    List<RateLimitApiObj> findRateLimitApis();
}