package com.smart4y.cloud.gateway.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.gateway.application.GatewayRouteService;
import com.smart4y.cloud.gateway.domain.entity.GatewayRoute;
import com.smart4y.cloud.gateway.infrastructure.persistence.mybatis.GatewayRouteMapper;
import com.smart4y.cloud.gateway.interfaces.dtos.GatewayRouteQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.exception.OpenAlertException;
import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.toolkit.base.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class GatewayRouteServiceImpl implements GatewayRouteService {

    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;

    @Override
    public PageInfo<GatewayRoute> findListPage(GatewayRouteQuery query) {
        PageHelper.startPage(query.getPage(), query.getLimit(), Boolean.TRUE);
        List<GatewayRoute> list = gatewayRouteMapper.selectAll();
        return new PageInfo<>(list);
    }

    @Override
    public List<GatewayRoute> findRouteList() {
        Weekend<GatewayRoute> queryWrapper = Weekend.of(GatewayRoute.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(GatewayRoute::getRouteState, "10");
        return gatewayRouteMapper.selectByExample(queryWrapper);
    }

    @Override
    public GatewayRoute getRoute(Long routeId) {
        return gatewayRouteMapper.selectByPrimaryKey(routeId);
    }

    @Override
    public long addRoute(GatewayRoute route) {
        if (StringHelper.isBlank(route.getRoutePath())) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "path不能为空！");
        }
        if (isExist(route.getRouteName())) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "路由名称已存在！");
        }
        gatewayRouteMapper.insertSelective(route);
        return route.getRouteId();
    }

    @Override
    public void updateRoute(GatewayRoute route) {
        if (StringHelper.isBlank(route.getRoutePath())) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "path不能为空！");
        }
        GatewayRoute saved = getRoute(route.getRouteId());
        if (saved == null) {
            throw new OpenAlertException(MessageType.BAD_REQUEST, "路由信息不存在!");
        }
        if (!saved.getRouteName().equals(route.getRouteName())) {
            // 和原来不一致重新检查唯一性
            if (isExist(route.getRouteName())) {
                throw new OpenAlertException(MessageType.BAD_REQUEST, "路由名称已存在!");
            }
        }
        gatewayRouteMapper.updateByPrimaryKeySelective(route);
    }

    @Override
    public void removeRoute(Long routeId) {
        GatewayRoute saved = getRoute(routeId);
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