package com.smart4y.cloud.common.messaging.configuration;

import com.smart4y.cloud.common.messaging.QueueConstant;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Youtao
 * on 2020/7/28 11:14
 */
@ComponentScan(basePackageClasses = {QueueConstant.class})
public class MessagingConfiguration {
}