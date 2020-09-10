package com.smart4y.cloud.hippo.domain.service;

import com.smart4y.cloud.hippo.interfaces.dtos.route.CreateRouteCommand;
import com.smart4y.cloud.hippo.interfaces.dtos.route.ModifyRouteCommand;
import com.smart4y.cloud.core.annotation.DomainService;
import com.smart4y.cloud.core.exception.OpenAlertException;
import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.hippo.domain.entity.GatewayRoute;
import com.smart4y.cloud.mapper.BaseDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;

/**
 * @author Youtao on 2020/8/19 14:28
 */
@DomainService
public class RouteService extends BaseDomainService<GatewayRoute> {

    /**
     * 获取分页列表
     *
     * @param pageNo    页码
     * @param pageSize  页大小
     * @param routeName 名称
     * @param routePath 前缀
     * @return 分页列表
     */
    public Page<GatewayRoute> getPageLike(int pageNo, int pageSize, String routeName, String routePath) {
        Weekend<GatewayRoute> weekend = Weekend.of(GatewayRoute.class);
        WeekendCriteria<GatewayRoute, Object> criteria = weekend.weekendCriteria();
        if (StringUtils.isNotBlank(routeName)) {
            criteria.andLike(GatewayRoute::getRouteName, "%" + routeName + "%");
        }
        if (StringUtils.isNotBlank(routePath)) {
            criteria.andLike(GatewayRoute::getRoutePath, "%" + routePath + "%");
        }
        weekend
                .orderBy("createdDate").desc();

        return this.findPage(weekend, pageNo, pageSize);
    }

    /**
     * 添加路由
     *
     * @param command 路由信息
     */
    public void createRoute(CreateRouteCommand command) {
        GatewayRoute record = new GatewayRoute();
        BeanUtils.copyProperties(command, record);
        record.setCreatedDate(LocalDateTime.now());
        this.save(record);
    }

    /**
     * 更新路由
     *
     * @param routeId 路由ID
     * @param command 路由信息
     */
    public void modifyRoute(long routeId, ModifyRouteCommand command) {
        GatewayRoute record = new GatewayRoute();
        BeanUtils.copyProperties(command, record);
        record.setRouteId(routeId);
        record.setLastModifiedDate(LocalDateTime.now());
        this.updateSelectiveById(record);
    }

    /**
     * 移除路由
     *
     * @param routeId 路由ID
     */
    public void removeRoute(long routeId) {
        GatewayRoute record = this.getById(routeId);
        if (null == record) {
            throw new OpenAlertException(MessageType.NOT_FOUND, "当前记录不存在");
        }
        // #1 删除元素
        this.removeById(routeId);
    }
}