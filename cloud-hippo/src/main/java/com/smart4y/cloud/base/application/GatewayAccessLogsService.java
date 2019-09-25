package com.smart4y.cloud.base.application;

import com.smart4y.cloud.base.domain.model.GatewayAccessLogs;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.PageParams;

/**
 * 网关访问日志
 *
 * @author liuyadu
 */
public interface GatewayAccessLogsService {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<GatewayAccessLogs> findListPage(PageParams pageParams);
}