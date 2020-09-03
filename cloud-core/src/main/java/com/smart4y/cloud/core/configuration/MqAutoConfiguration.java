package com.smart4y.cloud.core.configuration;

import com.smart4y.cloud.core.constant.QueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * MQ 配置
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class MqAutoConfiguration {

    /**
     * direct模式，直接根据队列名称投递消息
     */
    @Bean
    public Queue apiResourceQueue() {
        Queue queue = new Queue(QueueConstants.QUEUE_SCAN_API_RESOURCE);
        log.info("Api Resource Queue {} [{}]", QueueConstants.QUEUE_SCAN_API_RESOURCE, queue);
        return queue;
    }
}