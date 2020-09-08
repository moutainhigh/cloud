package com.smart4y.cloud.sms.controller;

import com.smart4y.cloud.sms.domain.NoticeInfo;
import com.smart4y.cloud.sms.domain.VerifyInfo;
import com.smart4y.cloud.sms.exception.VerificationCodeIsNullException;
import com.smart4y.cloud.sms.exception.VerifyFailException;
import com.smart4y.cloud.sms.service.NoticeService;
import com.smart4y.cloud.sms.service.VerificationCodeService;
import com.smart4y.cloud.sms.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信Controller
 */
@RestController
public class SmsController {

    /**
     * 手机验证码服务
     */
    private final VerificationCodeService verificationCodeService;
    /**
     * 短信通知服务
     */
    private final NoticeService noticeService;

    @Autowired
    public SmsController(VerificationCodeService verificationCodeService, NoticeService noticeService) {
        this.verificationCodeService = verificationCodeService;
        this.noticeService = noticeService;
    }

    /**
     * 获取验证码
     *
     * @param phone 手机号码
     */
    public void sendVerificationCode(@PathVariable("phone") String phone) {
        verificationCodeService.send(phone);
    }

    /**
     * 获取验证码
     *
     * @param phone              手机号码
     * @param identificationCode 识别码
     * @return 验证码信息
     */
    public VerifyInfo getVerificationCode(@PathVariable("phone") String phone,
                                          @RequestParam(value = "identificationCode", required = false, defaultValue = "") String identificationCode) {
        String code = verificationCodeService.find(phone, identificationCode);

        if (StringUtils.isBlank(code)) {
            throw new VerificationCodeIsNullException();
        }

        VerifyInfo info = new VerifyInfo();
        info.setCode(code);
        info.setIdentificationCode(identificationCode);
        info.setPhone(phone);

        return info;
    }

    /**
     * 验证信息
     *
     * @param verifyInfo 验证信息
     */
    public void verifyVerificationCode(@RequestBody VerifyInfo verifyInfo) {
        if (!verificationCodeService.verify(verifyInfo.getPhone(), verifyInfo.getCode(),
                verifyInfo.getIdentificationCode())) {
            throw new VerifyFailException();
        }
    }

    /**
     * 发送通知
     *
     * @param info 通知内容
     */
    public void sendNotice(@RequestBody NoticeInfo info) {
        noticeService.send(info.getNoticeData(), info.getPhones());
    }
}
