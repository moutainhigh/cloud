package com.smart4y.cloud.uaa;

import com.smart4y.cloud.core.infrastructure.AbstractApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * 平台认证服务
 * 提供微服务间oauth2统一平台认证服务
 * 提供认证客户端、令牌、已授权管理`
 * </p>
 * <p>
 * 移动应用认证中心
 * </p>
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class OwlApplication extends AbstractApplication {

    public static void main(String[] args) {
        initial(SpringApplication.run(OwlApplication.class, args));
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext context = event.getApplicationContext();
        Environment env = context.getEnvironment();
        log.info("Application '{}' is Reading!", env.getProperty("spring.application.name"));
    }
}