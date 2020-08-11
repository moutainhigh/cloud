package com.smart4y.cloud.base.gateway.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.gateway.domain.model.GatewayAccessLogs;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayAccessLogsQuery;

/**
 * 网关访问日志
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface GatewayAccessLogsService {

    /**
     * 分页查询
     */
    PageInfo<GatewayAccessLogs> findListPage(GatewayAccessLogsQuery query);
}