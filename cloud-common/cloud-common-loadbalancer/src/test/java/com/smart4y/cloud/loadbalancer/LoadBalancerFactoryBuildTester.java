package com.smart4y.cloud.loadbalancer;

import com.smart4y.cloud.loadbalancer.factory.LoadBalancerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * factory build test
 */
public class LoadBalancerFactoryBuildTester {

    private static final LBType rule = LBType.Random;

    @Test
    public void notWithParamsStructure() {
        Assertions.assertNotNull(LoadBalancerFactory.build(rule));
    }

    @Test
    public void withParamsStructure() {
        Assertions.assertNotNull(LoadBalancerFactory.build(rule, new CopyOnWriteArrayList<>()));
    }

    @Test
    public void withNullParamsStructure() {
        Assertions.assertNotNull(LoadBalancerFactory.build(rule, null));
    }
}