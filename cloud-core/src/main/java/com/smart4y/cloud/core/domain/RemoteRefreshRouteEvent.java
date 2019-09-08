package com.smart4y.cloud.core.domain;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 自定义网关刷新远程事件
 *
 *  * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class RemoteRefreshRouteEvent extends RemoteApplicationEvent {

    private RemoteRefreshRouteEvent() {
    }

    public RemoteRefreshRouteEvent(Object source, String originService, String destinationService) {
        super(source, originService, destinationService);
    }
}