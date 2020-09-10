package com.smart4y.cloud.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart4y.cloud.sms.handler.SendHandler;
import com.smart4y.cloud.sms.loadbalancer.RandomSmsLoadBalancer;
import com.smart4y.cloud.sms.supplier.aliyun.AliyunAutoConfigure;
import com.smart4y.cloud.sms.supplier.qcloud.QCloudAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

@Slf4j
public class LoadbalancerTest {

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
    public void loadbalancerTest() {
        TestPropertyValues.of("sms.aliyun.enable=true").applyTo(context);
        TestPropertyValues.of("sms.qcloud.enable=true").applyTo(context);
        context.register(AliyunAutoConfigure.class);
        context.register(QCloudAutoConfigure.class);
        context.refresh();
        RandomSmsLoadBalancer loadBalancer = context.getBean(RandomSmsLoadBalancer.class);
        for (int i = 0; i < 10; i++) {
            SendHandler sendHandler = loadBalancer.choose();
            log.debug("loadBalancer: {}", loadBalancer);
            log.debug("loadBalancer choose: {}", sendHandler);
        }
//        Assert.assertNotNull(sendHandler);
    }
}