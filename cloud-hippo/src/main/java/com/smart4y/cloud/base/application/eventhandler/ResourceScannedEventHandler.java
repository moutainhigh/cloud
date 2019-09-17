package com.smart4y.cloud.base.application.eventhandler;

import com.smart4y.cloud.base.application.ApiService;
import com.smart4y.cloud.base.domain.model.BaseApi;
import com.smart4y.cloud.base.domain.service.BaseApiDomainService;
import com.smart4y.cloud.base.infrastructure.constants.RedisConstants;
import com.smart4y.cloud.core.domain.event.ResourceScannedEvent;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.constants.QueueConstants;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资源扫描 事件处理器
 *
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Slf4j
@Component
public class ResourceScannedEventHandler {

    private final OpenRestTemplate openRestTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final BaseApiDomainService baseApiDomainService;
    private final ApiService apiService;

    @Autowired
    public ResourceScannedEventHandler(RedisTemplate<String, String> redisTemplate, BaseApiDomainService baseApiDomainService, ApiService apiService, OpenRestTemplate openRestTemplate) {
        this.redisTemplate = redisTemplate;
        this.baseApiDomainService = baseApiDomainService;
        this.apiService = apiService;
        this.openRestTemplate = openRestTemplate;
    }

    @RabbitListener(queues = QueueConstants.QUEUE_SCAN_API_RESOURCE)
    public void handle(@Payload ResourceScannedEvent event) {
        log.info("资源扫描：{}", event);
        try {
            String serviceId = event.getApplication();
            String key = RedisConstants.SCAN_API_RESOURCE_KEY_PREFIX + serviceId;
            String value = redisTemplate.opsForValue().get(key);
            if (null != value) {
                // 未失效，不再更新资源
                return;
            }
            List<ResourceScannedEvent.Mapping> mappings = event.getMappings();

            // 查询库中的数据，若不存在则添加，存在则更新
            List<String> apiCodes = mappings.stream()
                    .map(ResourceScannedEvent.Mapping::getApiCode).collect(Collectors.toList());
            Map<String, BaseApi> baseApiMap = baseApiDomainService.getApis(apiCodes).stream()
                    .collect(Collectors.toMap(BaseApi::getApiCode, Function.identity()));
            // 组装数据
            List<BaseApi> items = mappings.stream()
                    .map(x -> {
                        BaseApi api = new BaseApi()
                                .setApiName(x.getApiName())
                                .setApiCode(x.getApiCode())
                                .setApiDesc(x.getApiDesc())
                                .setPath(x.getPath())
                                .setClassName(x.getClassName())
                                .setMethodName(x.getMethodName())
                                .setRequestMethod(x.getRequestMethod())
                                .setServiceId(x.getServiceId())
                                .setContentType(x.getContentType())
                                .setIsAuth(x.getIsAuth());
                        String apiCode = x.getApiCode();
                        if (baseApiMap.containsKey(apiCode)) {
                            api.setApiId(baseApiMap.get(apiCode).getApiId());
                        } else {
                            api
                                    .setIsOpen(BaseConstants.DISABLED)
                                    .setIsPersist(BaseConstants.ENABLED);
                        }
                        return api;
                    }).collect(Collectors.toList());
            // 新增或更新API
            apiService.modifyApis(items);

            // 清理无效权限API
            if (CollectionUtils.isNotEmpty(apiCodes)) {
                apiService.clearInvalidApis(serviceId, apiCodes);
                openRestTemplate.refreshGateway();
                redisTemplate.opsForValue().set(key, String.valueOf(apiCodes.size()), Duration.ofMinutes(3));
            }
        } catch (Exception e) {
            log.error("资源扫描处理异常：{}", e.getLocalizedMessage(), e);
        }
    }
}