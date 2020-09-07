package com.smart4y.cloud.gateway.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.gateway.domain.entity.GatewayRoute;
import com.smart4y.cloud.gateway.interfaces.dtos.GatewayRouteQuery;

import java.util.List;

/**
 * 路由管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface GatewayRouteService {

    /**
     * 分页查询
     */
    PageInfo<GatewayRoute> findListPage(GatewayRouteQuery query);

    /**
     * 查询可用路由列表
     *
     * @return
     */
    List<GatewayRoute> findRouteList();

    /**
     * 获取路由信息
     *
     * @param routeId
     * @return
     */
    GatewayRoute getRoute(Long routeId);

    /**
     * 添加路由
     *
     * @param route
     */
    long addRoute(GatewayRoute route);

    /**
     * 更新路由
     *
     * @param route
     */
    void updateRoute(GatewayRoute route);

    /**
     * 删除路由
     *
     * @param routeId
     */
    void removeRoute(Long routeId);

    /**
     * 是否存在
     *
     * @param routeName
     * @return
     */
    Boolean isExist(String routeName);
}