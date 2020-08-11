package com.smart4y.cloud.base.access.application.eventhandler;

import com.smart4y.cloud.base.access.domain.model.RbacOperation;
import com.smart4y.cloud.base.access.domain.service.OperationService;
import com.smart4y.cloud.base.infrastructure.constants.RedisConstants;
import com.smart4y.cloud.core.constant.QueueConstants;
import com.smart4y.cloud.core.event.ResourceScannedEvent;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 资源扫描 事件处理器
 *
 * @author Youtao
 * Created by youtao on 2019/9/17.
 */
@Slf4j
@Component
public class ResourceScannedEventHandler {

    @Autowired
    private OpenRestTemplate openRestTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private OperationService operationService;

    @RabbitListener(queues = QueueConstants.QUEUE_SCAN_API_RESOURCE)
    public void handle(@Payload ResourceScannedEvent event) {
        log.info("资源扫描：{}", event);
        try {
            String serviceId = event.getApplication();
            String key = RedisConstants.SCAN_API_RESOURCE_KEY_PREFIX + serviceId;
            String value = this.redisTemplate.opsForValue().get(key);
            if (null != value) {
                // 未失效，不再更新资源
                return;
            }
            List<ResourceScannedEvent.Mapping> mappings = event.getMappings();
            LocalDateTime now = LocalDateTime.now();
            List<RbacOperation> apis = mappings.stream()
                    .map(x -> new RbacOperation()
                            .setOperationCode(x.getApiCode())
                            .setOperationName(x.getApiName())
                            .setOperationDesc(x.getApiDesc())
                            .setOperationMethod(x.getRequestMethod())
                            .setOperationMethodName(x.getMethodName())
                            .setOperationContentType(x.getContentType())
                            .setOperationServiceId(x.getServiceId())
                            .setOperationPath(x.getPath())
                            .setOperationAuth(x.getOperationAuth())
                            .setOperationClassName(x.getClassName())
                            .setCreatedDate(now)
                            .setLastModifiedDate(now))
                    .collect(Collectors.toList());
            List<String> codes = apis.stream().map(RbacOperation::getOperationCode).collect(Collectors.toList());

            for (RbacOperation api : apis) {
                codes.add(api.getOperationCode());

                Optional<RbacOperation> operationOptional = operationService.getByCode(api.getOperationCode());
                if (operationOptional.isPresent()) {
                    api
                            .setOperationId(operationOptional.get().getOperationId())
                            .setLastModifiedDate(LocalDateTime.now());
                    operationService.updateSelectiveById(api);
                } else {
                    api
                            .setOperationAuth(true)
                            .setOperationOpen(true)
                            .setOperationState("10")
                            .setCreatedDate(LocalDateTime.now());
                    operationService.save(api);
                }
            }
            if (CollectionUtils.isNotEmpty(apis)) {
                // TODO 清理无效权限数据
                log.info("有效数据：{}", codes.size());

                openRestTemplate.refreshGateway();
                this.redisTemplate.opsForValue().set(key, String.valueOf(apis.size()), Duration.ofMinutes(3));
            }
            log.info("资源扫描完成 - 服务名：{}，资源数量：{}", event.getApplication(), event.getMappings().size());
        } catch (Exception e) {
            log.error("资源扫描处理异常：{}", e.getLocalizedMessage(), e);
        }
    }
}