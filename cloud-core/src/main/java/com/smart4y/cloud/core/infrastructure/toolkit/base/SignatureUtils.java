package com.smart4y.cloud.core.infrastructure.toolkit.base;

import com.smart4y.cloud.core.infrastructure.constants.CommonConstants;
import com.smart4y.cloud.core.infrastructure.toolkit.Kit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@Slf4j
public class SignatureUtils {

    /**
     * 5分钟有效期
     */
    private final static long MAX_EXPIRE = 5 * 60;

    //public static void main(String[] args) {
    //    // 参数签名算法测试例子
    //    HashMap<String, String> signMap = new HashMap<>();
    //    signMap.put("clientId", "gateway");
    //    signMap.put("signType", "SHA256");
    //    signMap.put("timestamp", Kit.help().date().getTimestampStr());
    //    signMap.put("nonce", "d3c6fcd551104c53b4ccde31059d815a");
    //    String sign = SignatureUtils.getSign(signMap, "123456");
    //    System.out.println("签名结果:" + sign);
    //    signMap.put("sign", sign);
    //    System.out.println(SignatureUtils.validateSign(signMap, "123456"));
    //}

    /**
     * 验证参数
     */
    public static void validateParams(Map<String, String> paramsMap) throws Exception {
        Assert.notNull(paramsMap.get(CommonConstants.SIGN_CLIENT_ID_KEY), "签名验证失败:clientId不能为空");
        Assert.notNull(paramsMap.get(CommonConstants.SIGN_NONCE_KEY), "签名验证失败:nonce不能为空");
        Assert.notNull(paramsMap.get(CommonConstants.SIGN_TIMESTAMP_KEY), "签名验证失败:timestamp不能为空");
        Assert.notNull(paramsMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY), "签名验证失败:ignType不能为空");
        Assert.notNull(paramsMap.get(CommonConstants.SIGN_SIGN_KEY), "签名验证失败:sign不能为空");
        if (!SignatureUtils.SignType.contains(paramsMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY))) {
            throw new IllegalArgumentException(String.format("签名验证失败:signType必须为:%s,%s", SignatureUtils.SignType.MD5, SignatureUtils.SignType.SHA256));
        }
        try {
            String timestampKey = paramsMap.get(CommonConstants.SIGN_TIMESTAMP_KEY);
            DateUtils.parseDate(timestampKey, "yyyyMMddHHmmss");
        } catch (ParseException e) {
            throw new IllegalArgumentException("签名验证失败:timestamp格式必须为:yyyyMMddHHmmss");
        }
    }

    /**
     * @param paramMap 必须包含
     */
    public static boolean validateSign(Map<String, String> paramMap, String clientSecret) {
        try {
            validateParams(paramMap);
            String sign = paramMap.get(CommonConstants.SIGN_SIGN_KEY);
            String timestamp = paramMap.get(CommonConstants.SIGN_TIMESTAMP_KEY);
            long clientTimestamp = Long.parseLong(timestamp);
            // 判断时间戳 timestamp=201808091113
            if ((Kit.help().date().getTimestamp() - clientTimestamp) > MAX_EXPIRE) {
                log.debug("validateSign fail timestamp expire");
                return false;
            }
            //重新生成签名
            String signNew = getSign(paramMap, clientSecret);
            //判断当前签名是否正确
            if (signNew.equals(sign)) {
                return true;
            }
        } catch (Exception e) {
            log.error("validateSign error:{}", e.getMessage());
            return false;
        }
        return false;
    }


    /**
     * 得到签名
     *
     * @param paramMap     参数集合不含clientSecret
     *                     必须包含clientId=客户端ID
     *                     signType = SHA256|MD5 签名方式
     *                     timestamp=时间戳
     *                     nonce=随机字符串
     * @param clientSecret 验证接口的clientSecret
     */
    public static String getSign(Map<String, String> paramMap, String clientSecret) {
        if (paramMap == null) {
            return "";
        }
        //排序
        Set<String> keySet = paramMap.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        String signType = paramMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY);
        SignType type = null;
        if (StringHelper.isNotBlank(signType)) {
            type = SignType.valueOf(signType);
        }
        if (type == null) {
            type = SignType.MD5;
        }
        for (String k : keyArray) {
            if (k.equals(CommonConstants.SIGN_SIGN_KEY) || k.equals(CommonConstants.SIGN_CLIENT_SECRET_KEY)) {
                continue;
            }
            if (paramMap.get(k).trim().length() > 0) {
                // 参数值为空，则不参与签名
                sb.append(k).append("=").append(paramMap.get(k).trim()).append("&");
            }
        }
        //暂时不需要个人认证
        sb.append(CommonConstants.SIGN_CLIENT_SECRET_KEY + "=").append(clientSecret);
        String signStr = "";
        //加密
        switch (type) {
            case MD5:
                signStr = DigestUtils.md5Hex(sb.toString()).toLowerCase();
                break;
            case SHA256:
                signStr = DigestUtils.sha256Hex(sb.toString()).toLowerCase();
                break;
            default:
                break;
        }
        return signStr;
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
