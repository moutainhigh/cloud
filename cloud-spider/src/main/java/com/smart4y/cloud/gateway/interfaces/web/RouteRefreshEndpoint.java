package com.smart4y.cloud.gateway.interfaces.web;

import com.smart4y.cloud.core.event.RouteRemoteRefreshedEvent;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.gateway.interfaces.command.RefreshRouteCommand;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.cloud.bus.endpoint.AbstractBusEndpoint;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 自定义网关监控端点
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@RestControllerEndpoint(
        id = "gateways"
)
public class RouteRefreshEndpoint extends AbstractBusEndpoint {

    public RouteRefreshEndpoint(ApplicationEventPublisher context, String id) {
        super(context, id);
    }

    /**
     * 支持灰度发布
     * /actuator/open/refresh?destination = customers：**
     */
    @PostMapping("/routes/refresh")
    public ResultMessage<Void> busRefresh(@RequestBody RefreshRouteCommand command) {
        this.publish(new RouteRemoteRefreshedEvent(this, this.getInstanceId(), command.getDestination()));
        return ResultMessage.ok();
    }
}