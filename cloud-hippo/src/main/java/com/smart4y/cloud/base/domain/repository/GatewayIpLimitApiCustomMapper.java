package com.smart4y.cloud.base.domain.repository;

import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GatewayIpLimitApiCustomMapper {

    /**
     * 黑名单
     */
    List<IpLimitApiDTO> selectBlackList();

    /**
     * 白名单
     */
    List<IpLimitApiDTO> selectWhiteList();
}