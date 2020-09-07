package com.smart4y.cloud.base.access.application.eventhandler;

import com.smart4y.cloud.base.access.application.PrivilegeApplicationService;
import com.smart4y.cloud.base.access.domain.entity.RbacOperation;
import com.smart4y.cloud.base.system.infrastructure.constant.RedisConstants;
import com.smart4y.cloud.core.constant.QueueConstants;
import com.smart4y.cloud.core.event.ResourceScannedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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

    private final RedisTemplate<String, String> redisTemplate;
    private final PrivilegeApplicationService privilegeApplicationService;

    @Autowired
    public ResourceScannedEventHandler(RedisTemplate<String, String> redisTemplate, PrivilegeApplicationService privilegeApplicationService) {
        this.redisTemplate = redisTemplate;
        this.privilegeApplicationService = privilegeApplicationService;
    }

    @RabbitListener(queues = QueueConstants.QUEUE_SCAN_API_RESOURCE)
    public void handle(@Payload ResourceScannedEvent event) {
        log.info("资源扫描......");
        try {
            String serviceId = event.getApplication();
            String key = RedisConstants.SCAN_API_RESOURCE_KEY_PREFIX + serviceId;
            String value = this.redisTemplate.opsForValue().get(key);
            if (null != value) {
                // 未失效，不再更新资源
                return;
            }
            // 转换数据
            List<RbacOperation> operations = convertForOperations(event);

            // 同步数据
            privilegeApplicationService.syncServiceOperation(serviceId, operations);

            log.info("资源扫描完成 - 服务名：{}，资源数量：{}", event.getApplication(), event.getMappings().size());
        } catch (Exception e) {
            log.error("资源扫描处理异常：{}", e.getLocalizedMessage(), e);
        }
    }

    /**
     * 事件数据转为操作数据
     */
    private List<RbacOperation> convertForOperations(ResourceScannedEvent event) {
        if (CollectionUtils.isEmpty(event.getMappings())) {
            return Collections.emptyList();
        }
        return event.getMappings().stream()
                .filter(x -> !x.getPath().contains("/swagger-resources"))
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
                        .setOperationClassName(x.getClassName()))
                .collect(Collectors.toList());
    }
}