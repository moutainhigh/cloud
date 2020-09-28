package com.smart4y.cloud.sms.loadbalancer;

import com.smart4y.cloud.loadbalancer.LBType;
import com.smart4y.cloud.loadbalancer.impl.WeightRoundRobinLoadBalancer;
import com.smart4y.cloud.sms.domain.NoticeData;
import com.smart4y.cloud.sms.handler.SendHandler;

/**
 * 加权轮询
 */
public class WeightRoundRobinSmsLoadBalancer extends WeightRoundRobinLoadBalancer<SendHandler, NoticeData> implements SmsSenderLoadBalancer {

    public static final LBType TYPE_NAME = LBType.WeightRoundRobin;
}