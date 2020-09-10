package com.smart4y.cloud.sms.supplier.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart4y.cloud.sms.domain.NoticeData;
import com.smart4y.cloud.sms.handler.SendHandler;
import com.smart4y.cloud.sms.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * 阿里云短信发送处理
 */
@Slf4j
public class AliyunSendHandler implements SendHandler {

    private static final String OK = "OK";
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    private final AliyunProperties properties;
    private final ObjectMapper objectMapper;
    private final IAcsClient acsClient;

    /**
     * 构造阿里云短信发送处理
     *
     * @param properties   阿里云短信配置
     * @param objectMapper objectMapper
     */
    public AliyunSendHandler(AliyunProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;

        String endPoint = properties.getEndpoint();
        String accessKeyId = properties.getAccessKeyId();
        String accessKeySecret = properties.getAccessKeySecret();

        IClientProfile profile = DefaultProfile.getProfile(endPoint, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(endPoint, PRODUCT, DOMAIN);

        acsClient = new DefaultAcsClient(profile);
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        if (!isEnable()) {
            log.warn("未启用");
            return false;
        }

        String paramString;
        try {
            paramString = objectMapper.writeValueAsString(noticeData.getParams());
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return false;
        }

        SendSmsRequest request = new SendSmsRequest();
        request.setSysMethod(MethodType.POST);
        request.setPhoneNumbers(StringUtils.join(phones, ","));
        request.setSignName(properties.getSignName());
        request.setTemplateCode(properties.getTemplates(noticeData.getType()));
        request.setTemplateParam(paramString);

        try {
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            if (OK.equals(sendSmsResponse.getCode())) {
                return true;
            }

            log.debug("send fail[code={}, message={}]", sendSmsResponse.getCode(), sendSmsResponse.getMessage());
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

        return false;
    }

    @Override
    public boolean isEnable() {
        return properties.isEnable();
    }
}
