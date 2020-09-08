package com.smart4y.cloud.sms.loadbalancer;

import com.smart4y.cloud.loadbalancer.ILoadBalancer;
import com.smart4y.cloud.sms.domain.NoticeData;
import com.smart4y.cloud.sms.handler.SendHandler;

/**
 *
 */
public interface SmsSenderLoadBalancer extends ILoadBalancer<SendHandler, NoticeData> {
}