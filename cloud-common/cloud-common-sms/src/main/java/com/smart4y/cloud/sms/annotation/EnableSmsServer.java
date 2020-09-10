package com.smart4y.cloud.sms.annotation;

import com.smart4y.cloud.sms.autoconfigure.SmsConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用短信服务
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({SmsConfiguration.class})
public @interface EnableSmsServer {
}