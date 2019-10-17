package com.smart4y.cloud.gateway;

import com.smart4y.cloud.core.infrastructure.AbstractApplication;
import com.smart4y.cloud.gateway.infrastructure.locator.JdbcRouteDefinitionLocator;
import com.smart4y.cloud.gateway.infrastructure.locator.ResourceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
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
@RemoteApplicationEventScan(basePackages = "com.smart4y.cloud")
public class SpiderApplication extends AbstractApplication implements CommandLineRunner {

    private final ResourceLocator resourceLocator;
    private final JdbcRouteDefinitionLocator jdbcRouteDefinitionLocator;

    @Autowired
    public SpiderApplication(ResourceLocator resourceLocator, JdbcRouteDefinitionLocator jdbcRouteDefinitionLocator) {
        this.resourceLocator = resourceLocator;
        this.jdbcRouteDefinitionLocator = jdbcRouteDefinitionLocator;
    }

    public static void main(String[] args) {
        initial(SpringApplication.run(SpiderApplication.class, args));
    }

    @Override
    public void run(String... args) {
        jdbcRouteDefinitionLocator.refresh();
        resourceLocator.refresh();
    }
}