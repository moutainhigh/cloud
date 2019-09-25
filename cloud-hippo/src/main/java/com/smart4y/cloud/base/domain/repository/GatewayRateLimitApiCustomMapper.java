package com.smart4y.cloud.base.domain.repository;

import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GatewayRateLimitApiCustomMapper {

    /**
     * 查询接口流量限制
     */
    List<RateLimitApiDTO> selectRateLimitApi();
}