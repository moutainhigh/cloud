package com.smart4y.cloud.core.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * 应用启动类 基类
 *
 * @author Youtao
 *         Created by youtao on 2018/11/15.
 */
@Slf4j
public abstract class AbstractApplication {

    /* ****************************************************************
        public static void main(String[] args) {
            ConfigurableApplicationContext context = SpringApplication
                    .run(ZuulGatewayApplication.class, args);
            initial(context);
        }
    *******************************************************************/

    /**
     * 初始化应用信息
     *
     * @param context {@link ConfigurableApplicationContext}
     */
    protected static void initial(ConfigurableApplicationContext context) {
        try {
            Environment env = context.getEnvironment();
            log.info("\n----------------------------------------------------------\n\t" +
                            "Application '{}' is running! Access URLs:\n\t" +
                            "Local: \t\thttp://127.0.0.1:{}\n\t" +
                            "External: \thttp://{}:{}\n\t" +
                            "Profile(s): \t{}" +
                            "\n----------------------------------------------------------",
                    env.getProperty("spring.application.name"),
                    env.getProperty("server.port"),
                    InetAddress.getLocalHost().getHostAddress(),
                    env.getProperty("server.port"),
                    Arrays.toString(env.getActiveProfiles()));

            log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}