package com.smart4y.cloud.sms.service;

import com.smart4y.cloud.sms.domain.NoticeData;
import com.smart4y.cloud.sms.entity.VerificationCode;
import com.smart4y.cloud.sms.exception.PhoneIsNullException;
import com.smart4y.cloud.sms.exception.RetryTimeShortException;
import com.smart4y.cloud.sms.properties.SmsProperties;
import com.smart4y.cloud.sms.repository.IVerificationCodeRepository;
import com.smart4y.cloud.sms.utils.RandomUtil;
import com.smart4y.cloud.sms.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 手机验证码服务实现
 */
@Service
public class DefaultVerificationCodeService implements VerificationCodeService {

    private final IVerificationCodeRepository repository;
    private final SmsProperties properties;
    private final NoticeService noticeService;
    private final ICodeGenerate codeGenerate;

    @Autowired
    public DefaultVerificationCodeService(IVerificationCodeRepository repository, SmsProperties properties, NoticeService noticeService, ICodeGenerate codeGenerate) {
        this.repository = repository;
        this.properties = properties;
        this.noticeService = noticeService;
        this.codeGenerate = codeGenerate;
    }

    @Override
    public String find(String phone, String identificationCode) {
        if (StringUtils.isBlank(phone)) {
            return null;
        }

        phoneValidation(phone);

        VerificationCode verificationCode = repository.findOne(phone, identificationCode);

        return verificationCode == null ? null : verificationCode.getCode();
    }

    private String createIdentificationCode() {
        if (!properties.getVerificationCode().isUseIdentificationCode()) {
            return null;
        }

        return RandomUtil.nextString(properties.getVerificationCode().getIdentificationCodeLength());
    }

    @Override
    public void send(String tempPhone) {
        String phone = StringUtils.trimToNull(tempPhone);

        if (phone == null) {
            throw new PhoneIsNullException();
        }

        phoneValidation(phone);

        String identificationCode = createIdentificationCode();
        VerificationCode verificationCode = repository.findOne(phone, identificationCode);
        boolean newVerificationCode = false;

        Long expirationTime = properties.getVerificationCode().getExpirationTime();

        if (verificationCode == null) {
            verificationCode = new VerificationCode();
            verificationCode.setPhone(phone);
            verificationCode.setIdentificationCode(identificationCode);

            Long retryIntervalTime = properties.getVerificationCode().getRetryIntervalTime();

            if (expirationTime != null && expirationTime > 0) {
                verificationCode.setExpirationTime(LocalDateTime.now().plusSeconds(expirationTime));
            }
            if (retryIntervalTime != null && retryIntervalTime > 0) {
                verificationCode.setRetryTime(LocalDateTime.now().plusSeconds(retryIntervalTime));
            }

            verificationCode.setCode(codeGenerate.generate());
            newVerificationCode = true;
        } else {
            LocalDateTime retryTime = verificationCode.getRetryTime();

            if (retryTime != null) {
                long surplus =
                        retryTime.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

                if (surplus > 0) {
                    throw new RetryTimeShortException(surplus);
                }
            }
        }

        Map<String, String> params = new HashMap<>(4);
        params.put(MSG_KEY_CODE, verificationCode.getCode());
        if (verificationCode.getIdentificationCode() != null) {
            params.put(MSG_KEY_IDENTIFICATION_CODE, verificationCode.getIdentificationCode());
        }
        if (properties.getVerificationCode().isTemplateHasExpirationTime() && expirationTime != null
                && expirationTime > 0) {
            params.put(MSG_KEY_EXPIRATION_TIME_OF_SECONDS, String.valueOf(expirationTime));
            params.put(MSG_KEY_EXPIRATION_TIME_OF_MINUTES, String.valueOf(expirationTime / 60));
        }

        NoticeData notice = new NoticeData();
        notice.setType(VerificationCode.TYPE);
        notice.setParams(params);

        if (noticeService.send(notice, phone) && newVerificationCode) {
            repository.save(verificationCode);
        }
    }

    @Override
    public boolean verify(String phone, String code, String identificationCode) {
        if (StringUtils.isAnyBlank(phone, code)) {
            return false;
        }

        phoneValidation(phone);

        VerificationCode verificationCode = repository.findOne(phone, identificationCode);

        if (verificationCode == null) {
            return false;
        }

        boolean verifyData = Objects.equals(verificationCode.getCode(), code);

        if (verifyData && properties.getVerificationCode().isDeleteByVerifySucceed()) {
            repository.delete(phone, identificationCode);
        }

        if (!verifyData && properties.getVerificationCode().isDeleteByVerifyFail()) {
            repository.delete(phone, identificationCode);
        }

        return verifyData;
    }

    private void phoneValidation(String phone) {
        if (!noticeService.phoneRegValidation(phone)) {
            throw new PhoneIsNullException();
        }
    }

}
