package com.smart4y.cloud.base.gateway.application;

import com.smart4y.cloud.base.gateway.domain.entity.GatewayLog;
import com.smart4y.cloud.base.gateway.interfaces.dtos.log.LogPageQuery;
import com.smart4y.cloud.core.message.page.Page;

/**
 * 网关访问日志
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
public interface LogApplicationService {

    Page<GatewayLog> getLogsPage(LogPageQuery query);
}