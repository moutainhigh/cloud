package com.smart4y.cloud.core.infrastructure.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * 健康检查一直失败。主要是db检测失败，抛出以下错误
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class DbHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    private int check() {
        // 可以实现自定义的数据库检测逻辑
        return 0;
    }
}