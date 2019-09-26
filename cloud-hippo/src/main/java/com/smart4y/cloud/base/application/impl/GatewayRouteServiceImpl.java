package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.GatewayRouteService;
import com.smart4y.cloud.base.domain.model.GatewayRoute;
import com.smart4y.cloud.base.domain.repository.GatewayRouteMapper;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.Page;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.exception.OpenAlertException;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@ApplicationService
public class GatewayRouteServiceImpl implements GatewayRouteService {

    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;

    @Override
    public IPage<GatewayRoute> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), Boolean.TRUE);
        List<GatewayRoute> list = gatewayRouteMapper.selectAll();
        PageInfo<GatewayRoute> pageInfo = new PageInfo<>(list);
        IPage<GatewayRoute> page = new Page<>();
        page.setRecords(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }

    @Override
    public List<GatewayRoute> findRouteList() {
        Weekend<GatewayRoute> queryWrapper = Weekend.of(GatewayRoute.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(GatewayRoute::getStatus, BaseConstants.ENABLED);
        return gatewayRouteMapper.selectByExample(queryWrapper);
    }

    @Override
    public GatewayRoute getRoute(Long routeId) {
        return gatewayRouteMapper.selectByPrimaryKey(routeId);
    }

    @Override
    public void addRoute(GatewayRoute route) {
        if (StringUtils.isBlank(route.getPath())) {
            throw new OpenAlertException("path不能为空！");
        }
        if (isExist(route.getRouteName())) {
            throw new OpenAlertException("路由名称已存在！");
        }
        route.setIsPersist(0);
        gatewayRouteMapper.insertSelective(route);
    }

    @Override
    public void updateRoute(GatewayRoute route) {
        if (StringUtils.isBlank(route.getPath())) {
            throw new OpenAlertException("path不能为空！");
        }
        GatewayRoute saved = getRoute(route.getRouteId());
        if (saved == null) {
            throw new OpenAlertException("路由信息不存在!");
        }
        if (saved.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException("保留数据，不允许修改");
        }
        if (!saved.getRouteName().equals(route.getRouteName())) {
            // 和原来不一致重新检查唯一性
            if (isExist(route.getRouteName())) {
                throw new OpenAlertException("路由名称已存在!");
            }
        }
        gatewayRouteMapper.updateByPrimaryKeySelective(route);
    }

    @Override
    public void removeRoute(Long routeId) {
        GatewayRoute saved = getRoute(routeId);
        if (saved != null && saved.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException("保留数据，不允许删除");
        }
        gatewayRouteMapper.deleteByPrimaryKey(routeId);
    }

    @Override
    public Boolean isExist(String routeName) {
        Weekend<GatewayRoute> queryWrapper = Weekend.of(GatewayRoute.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(GatewayRoute::getRouteName, routeName);
        int count = gatewayRouteMapper.selectCountByExample(queryWrapper);
        return count > 0;
    }
}