package com.smart4y.cloud.base.gateway.application.impl;

import com.smart4y.cloud.base.gateway.application.LogApplicationService;
import com.smart4y.cloud.base.gateway.domain.entity.GatewayLog;
import com.smart4y.cloud.base.gateway.interfaces.dtos.log.LogPageQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.toolkit.base.StringHelper;
import com.smart4y.cloud.mapper.BaseDomainService;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class LogApplicationServiceImpl extends BaseDomainService<GatewayLog> implements LogApplicationService {

    @Override
    public Page<GatewayLog> getLogsPage(LogPageQuery query) {
        Weekend<GatewayLog> weekend = Weekend.of(GatewayLog.class);
        WeekendCriteria<GatewayLog, Object> criteria = weekend.weekendCriteria();
        if (StringHelper.isNotBlank(query.getLogPath())) {
            criteria
                    .andLike(GatewayLog::getLogPath, "%" + query.getLogPath() + "%");
        }
        if (StringHelper.isNotBlank(query.getLogIp())) {
            criteria
                    .andLike(GatewayLog::getLogIp, "%" + query.getLogIp() + "%");
        }
        if (StringHelper.isNotBlank(query.getLogServiceId())) {
            criteria
                    .andEqualTo(GatewayLog::getLogServiceId, "%" + query.getLogServiceId() + "%");
        }
        weekend
                .orderBy("logRequestTime").desc();

        return this.findPage(weekend, query.getPage(), query.getLimit());
    }
}