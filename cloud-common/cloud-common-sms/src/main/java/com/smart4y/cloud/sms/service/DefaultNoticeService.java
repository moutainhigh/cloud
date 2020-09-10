package com.smart4y.cloud.sms.service;

import com.smart4y.cloud.loadbalancer.LoadBalancer;
import com.smart4y.cloud.sms.domain.NoticeData;
import com.smart4y.cloud.sms.exception.NotFindSendHandlerException;
import com.smart4y.cloud.sms.handler.SendHandler;
import com.smart4y.cloud.sms.properties.SmsProperties;
import com.smart4y.cloud.sms.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 短信通知服务实现
 */
@Slf4j
@Service
public class DefaultNoticeService implements NoticeService {

    private final SmsProperties properties;
    private final LoadBalancer<SendHandler, NoticeData> smsSenderLoadbalancer;

    @Autowired
    public DefaultNoticeService(SmsProperties properties, LoadBalancer<SendHandler, NoticeData> smsSenderLoadbalancer) {
        this.properties = properties;
        this.smsSenderLoadbalancer = smsSenderLoadbalancer;
    }

    @Override
    public boolean phoneRegValidation(String phone) {
        return StringUtils.isNotBlank(phone) && (StringUtils.isBlank(properties.getReg()) || phone
                .matches(properties.getReg()));
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        if (noticeData == null) {
            log.debug("noticeData is null");
            return false;
        }

        if (phones == null || phones.isEmpty()) {
            log.debug("phones is empty");
            return false;
        }

        List<String> phoneList = phones.stream().filter(this::phoneRegValidation).collect(Collectors.toList());

        if (phoneList.isEmpty()) {
            log.debug("after filter phones is empty");
            return false;
        }

        SendHandler sendHandler = smsSenderLoadbalancer.choose(noticeData);

        if (sendHandler == null) {
            throw new NotFindSendHandlerException();
        }

        return sendHandler.send(noticeData, phones);
    }
}
