package com.smart4y.cloud.base.access.domain.service;

import com.smart4y.cloud.base.access.domain.entity.GatewayLog;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.toolkit.base.StringHelper;
import com.smart4y.cloud.mapper.BaseDomainService;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 * @author Youtao on 2020/8/19 14:19
 */
@DomainService
public class LogService extends BaseDomainService<GatewayLog> {

    /**
     * 获取分页列表
     *
     * @param pageNo       页码
     * @param pageSize     页大小
     * @param logPath      地址
     * @param logIp        IP
     * @param logServiceId 所属服务
     * @return 分页列表
     */
    public Page<GatewayLog> getPageLike(int pageNo, int pageSize, String logPath, String logIp, String logServiceId) {
        Weekend<GatewayLog> weekend = Weekend.of(GatewayLog.class);
        WeekendCriteria<GatewayLog, Object> criteria = weekend.weekendCriteria();
        if (StringHelper.isNotBlank(logPath)) {
            criteria
                    .andLike(GatewayLog::getLogPath, "%" + logPath + "%");
        }
        if (StringHelper.isNotBlank(logIp)) {
            criteria
                    .andLike(GatewayLog::getLogIp, "%" + logIp + "%");
        }
        if (StringHelper.isNotBlank(logServiceId)) {
            criteria
                    .andEqualTo(GatewayLog::getLogServiceId, "%" + logServiceId + "%");
        }
        weekend
                .orderBy("logRequestTime").desc();

        return this.findPage(weekend, pageNo, pageSize);
    }
}