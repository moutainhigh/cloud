package com.smart4y.cloud.spider;

import com.smart4y.cloud.core.AbstractApplication;
import com.smart4y.cloud.spider.infrastructure.locator.JdbcRouteDefinitionLocator;
import com.smart4y.cloud.spider.infrastructure.locator.ResourceLocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 网关服务
 * 接口调用统一入口、数字验签、身份认证、接口鉴权、接口限流、黑白名单限制
 * 开发环境下提供在线调试文档.
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
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

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext context = event.getApplicationContext();
        Environment env = context.getEnvironment();
        log.info("Application '{}' is Reading!", env.getProperty("spring.application.name"));
    }
}