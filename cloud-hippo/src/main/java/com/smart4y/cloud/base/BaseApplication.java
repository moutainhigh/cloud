package com.smart4y.cloud.base;

import com.smart4y.cloud.core.infrastructure.AbstractApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 平台基础服务
 * 提供系统用户、权限分配、资源、客户端管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@EnableCaching
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class BaseApplication extends AbstractApplication implements CommandLineRunner {

    public static void main(String[] args) {
        initial(SpringApplication.run(BaseApplication.class, args));
    }

    @Override
    public void run(String... args) {
    }
}