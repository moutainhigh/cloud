package com.smart4y.cloud.base.access.application.eventhandler;

import com.smart4y.cloud.base.access.application.PrivilegeApplicationService;
import com.smart4y.cloud.base.access.domain.entity.RbacOperation;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资源扫描 事件处理器
 *
 * @author Youtao
 * Created by youtao on 2019/9/17.
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class ResourceScannedEventHandler {

    @Autowired
    private OpenRestTemplate openRestTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private OperationService operationService;
    @Autowired
    private PrivilegeApplicationService privilegeApplicationService;

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
            // 事件数据转为操作数据
            List<RbacOperation> operations = convertOperations(event);

            // 新增或更新操作
            saveOrUpdateOperations(operations);

            // 清理无效权限数据
            removeInvalidPrivileges(serviceId, operations);

            // 新增权限、操作权限
            addPrivilegesAndPrivilegeOperations(serviceId);

            // 给角色（超级管理员）添加本次新增的权限
            addRolePrivileges(operations);

            openRestTemplate.refreshGateway();
            this.redisTemplate.opsForValue().set(key, String.valueOf(operations.size()), Duration.ofMinutes(3));
            log.info("资源扫描完成 - 服务名：{}，资源数量：{}", event.getApplication(), event.getMappings().size());
        } catch (Exception e) {
            log.error("资源扫描处理异常：{}", e.getLocalizedMessage(), e);
        }
    }

    /**
     * 新增权限、操作权限
     */
    private void addPrivilegesAndPrivilegeOperations(String serviceId) {
        privilegeApplicationService.addPrivilegeOperations(serviceId);
    }

    private void addRolePrivileges(Collection<RbacOperation> operations) {
        List<String> validOperationCodes = Objects.requireNonNull(operations).stream()
                .map(RbacOperation::getOperationCode).collect(Collectors.toList());
        privilegeApplicationService.addRolePrivilegesByOperations(validOperationCodes);
    }

    /**
     * 清理无效权限
     */
    private void removeInvalidPrivileges(String serviceId, List<RbacOperation> operations) {
        List<String> validOperationCodes = Objects.requireNonNull(operations).stream()
                .map(RbacOperation::getOperationCode).collect(Collectors.toList());
        privilegeApplicationService.removeInvalidPrivilegesByOperations(serviceId, validOperationCodes);
    }

    /**
     * 新增或更新操作表数据
     */
    private void saveOrUpdateOperations(List<RbacOperation> operations) {
        // 若有资源操作数据则新增或更新操作表
        if (CollectionUtils.isEmpty(operations)) {
            return;
        }
        List<String> validOperationCodes = Objects.requireNonNull(operations).stream()
                .map(RbacOperation::getOperationCode).collect(Collectors.toList());
        Map<String, RbacOperation> operationMap = operationService.getByCodes(validOperationCodes).stream()
                .collect(Collectors.toMap(RbacOperation::getOperationCode, Function.identity()));

        LocalDateTime now = LocalDateTime.now();
        List<RbacOperation> updateItems = operations.stream()
                .filter(x -> operationMap.containsKey(x.getOperationCode()))
                .peek(x -> x
                        .setOperationId(operationMap.get(x.getOperationCode()).getOperationId())
                        .setLastModifiedDate(now))
                .collect(Collectors.toList());
        operationService.updateSelectiveBatchById(updateItems);

        List<RbacOperation> saveItems = operations.stream()
                .filter(x -> !operationMap.containsKey(x.getOperationCode()))
                .peek(x -> x
                        .setOperationAuth(true)
                        .setOperationOpen(true)
                        .setOperationState("10")
                        .setCreatedDate(now))
                .collect(Collectors.toList());
        operationService.saveBatch(saveItems);
    }

    /**
     * 事件数据转为操作数据
     */
    private List<RbacOperation> convertOperations(ResourceScannedEvent event) {
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