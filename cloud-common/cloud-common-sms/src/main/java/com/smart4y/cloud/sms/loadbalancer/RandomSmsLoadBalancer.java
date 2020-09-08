package com.smart4y.cloud.sms.loadbalancer;

import com.smart4y.cloud.loadbalancer.impl.RandomLoadBalancer;
import com.smart4y.cloud.sms.domain.NoticeData;
import com.smart4y.cloud.sms.handler.SendHandler;

/**
 *
 */
public class RandomSmsLoadBalancer extends RandomLoadBalancer<SendHandler, NoticeData> implements SmsSenderLoadBalancer {
}