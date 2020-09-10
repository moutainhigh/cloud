package com.smart4y.cloud.sms.controller;

import com.smart4y.cloud.sms.domain.NoticeInfo;
import com.smart4y.cloud.sms.domain.VerifyInfo;
import com.smart4y.cloud.sms.exception.VerificationCodeIsNullException;
import com.smart4y.cloud.sms.exception.VerifyFailException;
import com.smart4y.cloud.sms.service.NoticeService;
import com.smart4y.cloud.sms.service.VerificationCodeService;
import com.smart4y.cloud.sms.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 短信Controller
 */
@RestController
@Api(tags = {"短信"})
public class SmsController {

    /**
     * 手机验证码服务
     */
    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 短信通知服务
     */
    @Autowired
    private NoticeService noticeService;

    /**
     * 给指定号码发送验证码
     *
     * @param phone 手机号码
     */
    @PostMapping("/sms/verifies/{phone}")
    @ApiOperation(value = "给指定号码发送验证码")
    public void sendVerifyCode(@PathVariable("phone") String phone) {
        verificationCodeService.send(phone);
    }

    /**
     * 获取指定号码的验证码信息
     *
     * @param phone              手机号码
     * @param identificationCode 识别码
     * @return 验证码信息
     */
    @GetMapping("/sms/verifies/{phone}")
    @ApiOperation(value = "获取指定号码的验证码信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号码", required = true, paramType = "path", dataType = "string", example = "18688888888"),
            @ApiImplicitParam(name = "identificationCode", value = "识别码", paramType = "query", dataType = "string", example = "xxx")
    })
    public VerifyInfo getVerifyCode(@PathVariable("phone") String phone, @RequestParam(value = "identificationCode", required = false, defaultValue = "") String identificationCode) {
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
     * 验证验证码是否有效
     *
     * @param verifyInfo 验证信息
     */
    @PostMapping("/sms/verifies")
    @ApiOperation(value = "验证验证码是否有效")
    public void verifyCode(@Validated @RequestBody VerifyInfo verifyInfo) {
        if (!verificationCodeService.verify(verifyInfo.getPhone(), verifyInfo.getCode(), verifyInfo.getIdentificationCode())) {
            throw new VerifyFailException();
        }
    }

    /**
     * 发送短信通知
     *
     * @param info 通知内容
     */
    @PostMapping("/sms/notices")
    @ApiOperation(value = "发送短信通知")
    public void sendNotice(@Validated @RequestBody NoticeInfo info) {
        noticeService.send(info.getNoticeData(), info.getPhones());
    }
}