package com.smart4y.cloud.base.application.eventhandler;

import com.smart4y.cloud.base.domain.model.GatewayAccessLogs;
import com.smart4y.cloud.base.domain.repository.GatewayAccessLogsMapper;
import com.smart4y.cloud.core.domain.event.LogAccessedEvent;
import com.smart4y.cloud.core.infrastructure.constants.QueueConstants;
import com.smart4y.cloud.core.infrastructure.toolkit.Kit;
import com.smart4y.cloud.core.infrastructure.toolkit.ip.IpHelper;
import com.smart4y.cloud.core.infrastructure.toolkit.ip.IpInfo;
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
 *         Created by youtao on 2019/9/17.
 */
@Slf4j
@Component
public class LogAccessedEventHandler {

    @Autowired
    private GatewayAccessLogsMapper gatewayAccessLogsMapper;
    private IpHelper ipHelper = Kit.help().ip();

    @RabbitListener(queues = QueueConstants.QUEUE_ACCESS_LOGS)
    public void handle(@Payload LogAccessedEvent event) {
        try {
            log.info("网关（日志）：{}", event);
            GatewayAccessLogs record = new GatewayAccessLogs()
                    .setAuthentication(event.getAuthentication())
                    .setCreatedDate(LocalDateTime.now())
                    .setError(event.getError())
                    .setHeaders(event.getHeaders())
                    .setHttpStatus(event.getHttpStatus())
                    .setIp(event.getIp())
                    .setMethod(event.getMethod())
                    .setParams(event.getParams())
                    .setPath(event.getPath())
                    .setRequestTime(event.getRequestTime())
                    .setResponseTime(event.getResponseTime())
                    .setServiceId(event.getServiceId())
                    .setUserAgent(event.getUserAgent());
            String ip = record.getIp();
            if (StringUtils.isNotBlank(ip)) {
                IpInfo info = ipHelper.of(ip);
                if (null != info) {
                    String region = String.format("%s|%s|%s|%s",
                            info.getCountry(), info.getProvince(), info.getCity(), info.getIsp());
                    record.setRegion(region);
                }
            }
            LocalDateTime responseTime = record.getResponseTime();
            LocalDateTime requestTime = record.getRequestTime();
            if (null != requestTime && null != responseTime) {
                long millis = ChronoUnit.MILLIS.between(requestTime, responseTime);
                record.setUseTime(millis);
            }
            gatewayAccessLogsMapper.insertSelective(record);
        } catch (Exception e) {
            log.error("网关（日志）处理异常：{}", e.getLocalizedMessage(), e);
        }
    }
}