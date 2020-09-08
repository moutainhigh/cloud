package com.smart4y.cloud.sms.service;

import com.smart4y.cloud.sms.properties.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 默认验证码生成
 */
@Component
public class DefaultCodeGenerate implements ICodeGenerate {

    private SmsProperties properties;

    @Autowired
    public void setProperties(SmsProperties properties) {
        this.properties = properties;
    }

    @Override
    public String generate() {
        int codeLength = properties.getVerificationCode().getCodeLength();

        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMaximumIntegerDigits(codeLength);
        format.setMinimumIntegerDigits(codeLength);

        return format.format(ThreadLocalRandom.current().nextInt((int) Math.pow(10, codeLength)));
    }

}
