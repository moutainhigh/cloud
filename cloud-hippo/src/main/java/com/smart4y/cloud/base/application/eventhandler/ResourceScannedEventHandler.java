package com.smart4y.cloud.base.application.eventhandler;

import com.google.common.collect.Lists;
import com.smart4y.cloud.base.application.BaseAuthorityService;
import com.smart4y.cloud.base.application.BaseOperationService;
import com.smart4y.cloud.base.domain.model.BaseOperation;
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
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private BaseOperationService baseOperationService;

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
            List<BaseOperation> apis = mappings.stream()
                    .map(x -> new BaseOperation()
                            //.setApiId()
                            //.setApiCategory()
                            //.setPriority()
                            //.setStatus()
                            //.setIsPersist()
                            //.setIsOpen()
                            .setApiCode(x.getApiCode())
                            .setApiName(x.getApiName())
                            .setApiDesc(x.getApiDesc())
                            .setRequestMethod(x.getRequestMethod())
                            .setContentType(x.getContentType())
                            .setServiceId(x.getServiceId())
                            .setPath(x.getPath())
                            .setIsAuth(x.getIsAuth())
                            .setClassName(x.getClassName())
                            .setMethodName(x.getMethodName())
                            .setCreatedDate(now)
                            .setLastModifiedDate(now))
                    .collect(Collectors.toList());
            List<String> codes = Lists.newArrayList();
            for (BaseOperation api : apis) {
                codes.add(api.getApiCode());
                BaseOperation save = baseOperationService.getApi(api.getApiCode());
                if (save == null) {
                    api.setIsOpen(0);
                    api.setIsPersist(1);
                    baseOperationService.addApi(api);
                } else {
                    api.setApiId(save.getApiId());
                    baseOperationService.updateApi(api);
                }
            }
            if (CollectionUtils.isNotEmpty(apis)) {
                // 清理无效权限数据
                baseAuthorityService.clearInvalidApi(serviceId, codes);
                openRestTemplate.refreshGateway();
                this.redisTemplate.opsForValue().set(key, String.valueOf(apis.size()), Duration.ofMinutes(3));
            }
            log.info("资源扫描完成：{}", event);
        } catch (Exception e) {
            log.error("资源扫描处理异常：{}", e.getLocalizedMessage(), e);
        }
    }
}