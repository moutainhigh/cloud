package com.smart4y.cloud.sms.loadbalancer;

import com.smart4y.cloud.loadbalancer.LoadBalancer;
import com.smart4y.cloud.sms.domain.NoticeData;
import com.smart4y.cloud.sms.handler.SendHandler;

/**
 * 短信发送负载均衡
 */
public interface SmsSenderLoadBalancer extends LoadBalancer<SendHandler, NoticeData> {
}