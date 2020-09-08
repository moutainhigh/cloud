package com.smart4y.cloud.sms.supplier.baiducloud;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.sms.SmsClient;
import com.baidubce.services.sms.SmsClientConfiguration;
import com.baidubce.services.sms.model.SendMessageV3Request;
import com.baidubce.services.sms.model.SendMessageV3Response;
import com.smart4y.cloud.sms.domain.NoticeData;
import com.smart4y.cloud.sms.handler.SendHandler;
import com.smart4y.cloud.sms.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * 百度云发送处理
 */
@Slf4j
public class BaiduCloudSendHandler implements SendHandler {

    private final BaiduCloudProperties properties;

    private final SmsClient client;

    public BaiduCloudSendHandler(BaiduCloudProperties properties) {
        this.properties = properties;

        SmsClientConfiguration config = new SmsClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(properties.getAccessKeyId(), properties.getSecretAccessKey()));
        config.setEndpoint(properties.getEndpoint());
        client = new SmsClient(config);
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        String type = noticeData.getType();

        String templateId = properties.getTemplates(type);

        if (templateId == null) {
            log.debug("templateId invalid");
            return false;
        }

        SendMessageV3Request request = new SendMessageV3Request();
        request.setMobile(StringUtils.join(phones, ","));
        request.setSignatureId("");
        request.setTemplate(templateId);
        request.setContentVar(noticeData.getParams());

        SendMessageV3Response response = client.sendMessage(request);

        if (response == null) {
            log.debug("send fail: not response");
        } else if (!response.isSuccess()) {
            log.debug("send fail: {}", response.getCode());
            return false;
        }

        return true;
    }
}
