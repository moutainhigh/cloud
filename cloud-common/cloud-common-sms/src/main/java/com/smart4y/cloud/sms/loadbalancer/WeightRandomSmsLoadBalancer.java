package com.smart4y.cloud.sms.loadbalancer;


import com.smart4y.cloud.loadbalancer.LBType;
import com.smart4y.cloud.loadbalancer.impl.WeightRandomLoadBalancer;
import com.smart4y.cloud.sms.domain.NoticeData;
import com.smart4y.cloud.sms.handler.SendHandler;

/**
 * 加权随机
 *
 * @author guer
 */
public class WeightRandomSmsLoadBalancer extends WeightRandomLoadBalancer<SendHandler, NoticeData> implements SmsSenderLoadBalancer {

    public static final LBType TYPE_NAME = LBType.WeightRandom;
}