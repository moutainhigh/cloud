package com.smart4y.cloud.core.domain.event;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 自定义网关远程刷新事件
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class RouteRemoteRefreshedEvent extends RemoteApplicationEvent {

    private RouteRemoteRefreshedEvent() {
    }

    public RouteRemoteRefreshedEvent(Object source, String originService, String destinationService) {
        super(source, originService, destinationService);
    }
}