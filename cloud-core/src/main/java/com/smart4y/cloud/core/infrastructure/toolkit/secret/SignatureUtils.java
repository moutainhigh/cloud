package com.smart4y.cloud.core.infrastructure.toolkit.secret;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.smart4y.cloud.core.infrastructure.constants.CommonConstants;
import com.smart4y.cloud.core.infrastructure.toolkit.Kit;
import com.smart4y.cloud.core.infrastructure.toolkit.base.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class SignatureUtils {

    /**
     * 5分钟有效期
     */
    private final static long MAX_EXPIRE = 5 * 60;

    public static void main(String[] args) {
        String clientSecret = "0osTIhce7uPvDKHz6aa67bhCukaKoYl4";
        //参数签名算法测试例子
        HashMap<String, String> signMap = new HashMap<>();
        signMap.put("APP_ID", "1552274783265");
        signMap.put("SIGN_TYPE", SignType.MD5.name());
        signMap.put("TIMESTAMP", DateHelper.getCurrentTimestampStr());
        signMap.put("NONCE", Kit.help().random().shortUuid().toUpperCase());
        String sign = SignatureUtils.getSign(signMap, clientSecret);
        System.out.println("签名结果:" + sign);
        signMap.put("SIGN", sign);
        System.out.println("签名参数:" + JSONObject.toJSONString(signMap));
        System.out.println(SignatureUtils.validateSign(signMap, clientSecret));
    }

    /**
     * 验证参数
     */
    public static void validateParams(Map<String, String> paramsMap) {
        Assert.hasText(paramsMap.get(CommonConstants.SIGN_APP_ID_KEY), "签名验证失败:APP_ID不能为空");
        Assert.hasText(paramsMap.get(CommonConstants.SIGN_NONCE_KEY), "签名验证失败:NONCE不能为空");
        Assert.hasText(paramsMap.get(CommonConstants.SIGN_TIMESTAMP_KEY), "签名验证失败:TIMESTAMP不能为空");
        Assert.hasText(paramsMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY), "签名验证失败:SIGN_TYPE不能为空");
        Assert.hasText(paramsMap.get(CommonConstants.SIGN_SIGN_KEY), "签名验证失败:SIGN不能为空");
        if (!SignatureUtils.SignType.contains(paramsMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY))) {
            throw new IllegalArgumentException(String.format("签名验证失败:SIGN_TYPE必须为:%s,%s", SignatureUtils.SignType.MD5, SignatureUtils.SignType.SHA256));
        }
        try {
            DateHelper.parseDate(paramsMap.get(CommonConstants.SIGN_TIMESTAMP_KEY), "yyyyMMddHHmmss");
        } catch (ParseException e) {
            throw new IllegalArgumentException("签名验证失败:TIMESTAMP格式必须为:yyyyMMddHHmmss");
        }
        String timestamp = paramsMap.get(CommonConstants.SIGN_TIMESTAMP_KEY);
        long clientTimestamp = Long.parseLong(timestamp);
        // 判断时间戳 timestamp=201910091113
        if ((DateHelper.getCurrentTimestamp() - clientTimestamp) > MAX_EXPIRE) {
            throw new IllegalArgumentException("签名验证失败:TIMESTAMP已过期");
        }
    }

    /**
     * @param paramsMap 必须包含
     */
    public static boolean validateSign(Map<String, String> paramsMap, String clientSecret) {
        try {
            validateParams(paramsMap);
            String sign = paramsMap.get(CommonConstants.SIGN_SIGN_KEY);
            //重新生成签名
            String signNew = getSign(paramsMap, clientSecret);

            return signNew.equals(sign);
        } catch (Exception e) {
            log.error("validateSign error:{}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 得到签名
     *
     * @param requestParams 参数集合不含appSecret
     *                      必须包含appId=客户端ID
     *                      signType = SHA256|MD5 签名方式
     *                      timestamp=时间戳
     *                      nonce=随机字符串
     * @param clientSecret  验证接口的clientSecret
     * @return Sign
     */
    public static String getSign(Map<String, String> requestParams, String clientSecret) {
        // 待签名字段
        Map<String, Object> signMap = new HashMap<>(requestParams);
        signMap.remove(CommonConstants.SIGN_SIGN_KEY);
        signMap.put(CommonConstants.SIGN_SECRET_KEY, clientSecret);
        String signType = requestParams.get(CommonConstants.SIGN_SIGN_TYPE_KEY);

        StringBuilder builder = new StringBuilder();
        List<String> sortedKeys = signMap.keySet().stream().sorted().collect(Collectors.toList());
        for (String key : sortedKeys) {
            Object value = signMap.get(key);
            if (ObjectUtils.isNotEmpty(value)) {
                builder.append(key).append("=").append(value).append("&");
            }
        }
        String signString = builder.toString();

        String sign;
        switch (SignType.valueOf(signType)) {
            case SHA256:
                sign = DigestUtil.sha256Hex(signString).toUpperCase();
                break;
            case MD5:
                sign = DigestUtil.md5Hex(signString).toUpperCase();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + SignType.valueOf(signType));
        }
        return sign;
    }

    public enum SignType {
        MD5,
        SHA256;

        public static boolean contains(String type) {
            for (SignType typeEnum : SignType.values()) {
                if (typeEnum.name().equals(type)) {
                    return true;
                }
            }
            return false;
        }
    }
}