package com.smart4y.cloud.gateway;

import com.smart4y.cloud.core.AbstractApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 网关服务
 * 接口调用统一入口、数字验签、身份认证、接口鉴权、接口限流、黑白名单限制
 * 开发环境下提供在线调试文档.
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication extends AbstractApplication implements CommandLineRunner {

    public static void main(String[] args) {
        initial(SpringApplication.run(GatewayApplication.class, args));
    }

    @Override
    public void run(String... args) {
    }
}