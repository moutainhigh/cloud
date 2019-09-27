package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.GatewayAccessLogsService;
import com.smart4y.cloud.base.domain.model.GatewayAccessLogs;
import com.smart4y.cloud.base.domain.repository.GatewayAccessLogsMapper;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.model.Page;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtils;
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
    public IPage<GatewayAccessLogs> findListPage(PageParams pageParams) {
        GatewayAccessLogs query = pageParams.mapToObject(GatewayAccessLogs.class);

        Weekend<GatewayAccessLogs> queryWrapper = Weekend.of(GatewayAccessLogs.class);
        WeekendCriteria<GatewayAccessLogs, Object> criteria = queryWrapper.weekendCriteria();
        if (StringUtils.isNotBlank(query.getPath())) {
            criteria.andLike(GatewayAccessLogs::getPath, query.getPath() + "%");
        }
        if (StringUtils.isNotBlank(query.getIp())) {
            criteria.andEqualTo(GatewayAccessLogs::getIp, query.getIp());
        }
        if (StringUtils.isNotBlank(query.getServiceId())) {
            criteria.andEqualTo(GatewayAccessLogs::getServiceId, query.getServiceId());
        }
        queryWrapper.orderBy("requestTime").desc();

        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), Boolean.TRUE);
        List<GatewayAccessLogs> list = gatewayAccessLogsMapper.selectByExample(queryWrapper);
        PageInfo<GatewayAccessLogs> pageInfo = new PageInfo<>(list);
        IPage<GatewayAccessLogs> page = new Page<>();
        page.setRecords(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }
}