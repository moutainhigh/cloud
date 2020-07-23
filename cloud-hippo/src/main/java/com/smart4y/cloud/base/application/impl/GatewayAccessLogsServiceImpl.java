package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.GatewayAccessLogsService;
import com.smart4y.cloud.base.domain.model.GatewayAccessLogs;
import com.smart4y.cloud.base.infrastructure.mapper.GatewayAccessLogsMapper;
import com.smart4y.cloud.base.interfaces.query.GatewayAccessLogsQuery;
import com.smart4y.cloud.core.application.ApplicationService;
import com.smart4y.cloud.core.infrastructure.toolkit.base.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class GatewayAccessLogsServiceImpl implements GatewayAccessLogsService {

    @Autowired
    private GatewayAccessLogsMapper gatewayAccessLogsMapper;

    @Override
    public PageInfo<GatewayAccessLogs> findListPage(GatewayAccessLogsQuery query) {
        Weekend<GatewayAccessLogs> queryWrapper = Weekend.of(GatewayAccessLogs.class);
        WeekendCriteria<GatewayAccessLogs, Object> criteria = queryWrapper.weekendCriteria();
        if (StringHelper.isNotBlank(query.getPath())) {
            criteria.andLike(GatewayAccessLogs::getPath, query.getPath() + "%");
        }
        if (StringHelper.isNotBlank(query.getIp())) {
            criteria.andEqualTo(GatewayAccessLogs::getIp, query.getIp());
        }
        if (StringHelper.isNotBlank(query.getServiceId())) {
            criteria.andEqualTo(GatewayAccessLogs::getServiceId, query.getServiceId());
        }
        queryWrapper.orderBy("requestTime").desc();

        PageHelper.startPage(query.getPage(), query.getLimit(), Boolean.TRUE);
        List<GatewayAccessLogs> list = gatewayAccessLogsMapper.selectByExample(queryWrapper);
        return new PageInfo<>(list);
    }
}