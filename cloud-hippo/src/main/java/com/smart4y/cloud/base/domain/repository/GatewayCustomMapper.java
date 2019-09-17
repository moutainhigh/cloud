package com.smart4y.cloud.base.domain.repository;

import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
     * 查询 黑白名单
     */
    List<IpLimitApiDTO> selectIpLimitApi(@Param("policyType") int policyType);

    /**
     * 查询 接口流量限制
     */
    List<RateLimitApiDTO> selectRateLimitApi();
}