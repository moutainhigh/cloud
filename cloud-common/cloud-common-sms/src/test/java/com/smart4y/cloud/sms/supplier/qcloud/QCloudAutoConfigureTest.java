package com.smart4y.cloud.sms.supplier.qcloud;

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

public class QCloudAutoConfigureTest {

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
        context.register(QCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(QCloudSendHandler.class));
    }

    @Test
    public void testConditionalOnPropertyEmpty() {
        TestPropertyValues.of("sms.qcloud.enable=").applyTo(context);
        context.register(QCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(QCloudSendHandler.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditionalOnPropertyFalse() {
        TestPropertyValues.of("sms.qcloud.enable=false").applyTo(context);
        context.register(QCloudAutoConfigure.class);
        context.refresh();
        context.getBean(QCloudSendHandler.class);
    }

    @Test
    public void testConditionalOnPropertyTrue() {
        TestPropertyValues.of("sms.qcloud.enable=true").applyTo(context);
        context.register(QCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(QCloudSendHandler.class));
    }
}
