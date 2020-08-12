package com.smart4y.cloud.base.gateway.application.eventhandler;

import com.smart4y.cloud.base.domain.service.IpHelper;
import com.smart4y.cloud.base.gateway.domain.model.GatewayLog;
import com.smart4y.cloud.base.gateway.domain.service.LogService;
import com.smart4y.cloud.core.constant.QueueConstants;
import com.smart4y.cloud.core.event.LogAccessedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 网关（日志）事件处理器
 *
 * @author Youtao
 * Created by youtao on 2019/9/17.
 */
@Slf4j
@Component
public class LogAccessedEventHandler {

    @Autowired
    private LogService logService;
    @Autowired
    private IpHelper ipHelper;

    @RabbitListener(queues = QueueConstants.QUEUE_ACCESS_LOGS)
    public void handle(@Payload LogAccessedEvent event) {
        try {
            log.info("网关（日志）：{}", event);
            GatewayLog record = new GatewayLog()
                    .setLogAuthentication(event.getAuthentication())
                    .setCreatedDate(LocalDateTime.now())
                    .setLogError(event.getError())
                    .setLogHeaders(event.getHeaders())
                    .setLogHttpStatus(event.getHttpStatus())
                    .setLogIp(event.getIp())
                    .setLogMethod(event.getMethod())
                    .setLogParams(event.getParams())
                    .setLogPath(event.getPath())
                    .setLogRequestTime(event.getRequestTime())
                    .setLogResponseTime(event.getResponseTime())
                    .setLogServiceId(event.getServiceId())
                    .setLogUserAgent(event.getUserAgent());
            String ip = record.getLogIp();
            if (StringUtils.isNotBlank(ip)) {
                IpHelper.IpInfo info = ipHelper.of(ip);
                if (null != info) {
                    record.setLogRegion(info.getDetail());
                }
            }
            LocalDateTime responseTime = record.getLogResponseTime();
            LocalDateTime requestTime = record.getLogRequestTime();
            if (null != requestTime && null != responseTime) {
                long millis = ChronoUnit.MILLIS.between(requestTime, responseTime);
                record.setLogUseMillis(millis);
            }
            logService.save(record);
        } catch (Exception e) {
            log.error("网关（日志）处理异常：{}", e.getLocalizedMessage(), e);
        }
    }
}