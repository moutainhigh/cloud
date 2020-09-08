package com.smart4y.cloud.sms.supplier.jdcloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart4y.cloud.sms.loadbalancer.RandomSmsLoadBalancer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class JdCloudAutoConfigureTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.registerBean("objectMapper", ObjectMapper.class);
        context.registerBean("smsSenderLoadbalancer", RandomSmsLoadBalancer.class);
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void testConditionalOnPropertyNull() {
        context.register(JdCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(JdCloudSendHandler.class));
    }

    @Test
    public void testConditionalOnPropertyEmpty() {
        TestPropertyValues.of("sms.jdcloud.enable=").applyTo(context);
        context.register(JdCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(JdCloudSendHandler.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditionalOnPropertyFalse() {
        TestPropertyValues.of("sms.jdcloud.enable=false").applyTo(context);
        context.register(JdCloudAutoConfigure.class);
        context.refresh();
        context.getBean(JdCloudSendHandler.class);
    }

    @Test
    public void testConditionalOnPropertyTrue() {
        TestPropertyValues.of("sms.jdcloud.enable=true").applyTo(context);
        context.register(JdCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(JdCloudSendHandler.class));
    }
}
