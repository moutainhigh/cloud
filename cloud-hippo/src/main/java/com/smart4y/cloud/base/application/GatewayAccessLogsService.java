package com.smart4y.cloud.base.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.domain.model.GatewayAccessLogs;
import com.smart4y.cloud.core.domain.PageParams;

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
    PageInfo<GatewayAccessLogs> findListPage(PageParams pageParams);
}