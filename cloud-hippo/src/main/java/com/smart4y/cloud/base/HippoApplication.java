package com.smart4y.cloud.base;

import com.smart4y.cloud.core.infrastructure.AbstractApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 平台基础服务
 * 提供系统用户、权限分配、资源、客户端管理
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@EnableCaching
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class HippoApplication extends AbstractApplication {

    public static void main(String[] args) {
        initial(SpringApplication.run(HippoApplication.class, args));
    }
}