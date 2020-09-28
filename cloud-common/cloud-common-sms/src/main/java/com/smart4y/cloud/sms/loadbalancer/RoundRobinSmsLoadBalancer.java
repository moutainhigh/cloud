package com.smart4y.cloud.sms.loadbalancer;

import com.smart4y.cloud.loadbalancer.LBType;
import com.smart4y.cloud.loadbalancer.impl.RoundRobinLoadBalancer;
import com.smart4y.cloud.sms.domain.NoticeData;
import com.smart4y.cloud.sms.handler.SendHandler;

/**
 * 轮询
 */
public class RoundRobinSmsLoadBalancer extends RoundRobinLoadBalancer<SendHandler, NoticeData> implements SmsSenderLoadBalancer {

    public static final LBType TYPE_NAME = LBType.RoundRobin;
}