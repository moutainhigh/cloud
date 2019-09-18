package com.smart4y.cloud.uaa;

import com.smart4y.cloud.core.infrastructure.AbstractApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

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
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class UaaApplication extends AbstractApplication implements CommandLineRunner {

    public static void main(String[] args) {
        initial(SpringApplication.run(UaaApplication.class, args));
    }

    @Override
    public void run(String... args) {
    }
}