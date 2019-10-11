package com.smart4y.cloud.core.infrastructure.toolkit.secret;

import com.smart4y.cloud.core.infrastructure.toolkit.Kit;
import lombok.extern.apachecommons.CommonsLog;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 密钥对：生成RSA密钥对
 * 加解密：私钥加密公钥解密、公钥加密私钥解密
 * 签名：明文+私钥
 * 验签：明文+公钥+签名
 *
 * @author Youtao
 *         Created by youtao on 2018/12/13.
 */
@CommonsLog
public enum RsaHelper {
    INSTANCE;

    /**
     * 数字签名，密钥算法
     */
    private static final String ALGORITHM = "RSA";
    /**
     * 数字签名签名/验证算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * RSA密钥长度，RSA算法的默认密钥长度是1024密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 1024;

    RsaHelper() {
    }

    public static void main(String[] args) {
        String rsaSecret = "x2318^^(*WRYQWR(QW&T";
        RsaData rsa = Kit.help().rsa().createRsa(rsaSecret);
        System.out.println(Kit.help().json().toPrettyJson(rsa));

        // 待加密数据
        String content = "admin123";
        System.out.println("待加密数据:\t" + content);

        RsaHelper helper = RsaHelper.INSTANCE;


        // 私钥加密
        String priEncodeString = helper.encodeByPriKey(content, rsa.getPriKey());
        System.out.println("私钥加密:\t" + priEncodeString);
        // 公钥解密
        String pubDecodeString = helper.decodeByPubKey(priEncodeString, rsa.getPubKey());
        System.out.println("公钥解密:\t" + pubDecodeString);

        // 公钥加密
        String pubEncodeString = helper.encodeByPubKey(content, rsa.getPubKey());
        System.out.println("公钥加密:\t" + pubEncodeString);
        // 私钥解密
        String priDecodeString = helper.decodeByPriKey(pubEncodeString, rsa.getPriKey());
        System.out.println("私钥解密:\t" + priDecodeString);

        String sign = helper.sign(content, rsa.getPriKey());
        System.out.println("私钥签名：\t" + sign);
        boolean flag = helper.verify(content, sign, rsa.getPubKey());
        System.out.println("公钥验签：\t" + flag);

        String userPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgPwBwWCXjIKdxo+xrt3eWwrXEDWNZofZ19AtSce0MLjLUW0u0mXMU54VlkUCTBfLQAIPNS2shP12djgqnVAJ9XfuBxjqfCm2BDzGBJmMn8YeC/YYzGs2k8x15Qpu5yWtHvHgmDT9UljKiMkGY1K4um6HNJRoYTaxIcjjRPmYSzQIDAQAB";
        PublicKey publicKey = helper.getPublicKey(userPubKey);
        System.out.println(new String(publicKey.getEncoded(), StandardCharsets.UTF_8));
    }

    /**
     * 生成公私钥
     *
     * @param rsaSecret 公私钥加密密码
     * @return 公私钥
     */
    public RsaData createRsa(String rsaSecret) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            SecureRandom secureRandom = new SecureRandom();
            // 初始化随机产生器
            secureRandom.setSeed(rsaSecret.getBytes(StandardCharsets.UTF_8));
            // 初始化密钥生成器
            keyPairGenerator.initialize(KEY_SIZE, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            String publicKeyString = toHexString(keyPair.getPublic().getEncoded());
            String privateKeyString = toHexString(keyPair.getPrivate().getEncoded());
            return new RsaData(publicKeyString, privateKeyString);
        } catch (Exception e) {
            log.error("生成公私钥错误" + e.getLocalizedMessage(), e);
            throw new IllegalArgumentException("生成公私钥错误");
        }
    }

    /**
     * 公钥加密
     *
     * @param content 加密前的明文数据
     * @param pubKey  公钥
     * @return 加密后的Base64字符串
     */
    public String encodeByPubKey(String content, String pubKey) {
        try {
            PublicKey publicKey = getPublicKey(pubKey);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enSign = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return toHexString(enSign);
        } catch (Exception e) {
            log.error("公钥加密错误" + e.getLocalizedMessage(), e);
            throw new IllegalArgumentException("公钥加密错误");
        }
    }

    /**
     * 获取 公钥
     *
     * @param pubKey 公钥
     * @return 公钥
     */
    public PublicKey getPublicKey(String pubKey) {
        try {
            byte[] pubKeyBytes = toBytes(pubKey);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKeyBytes);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            log.error("获取公钥错误" + e.getLocalizedMessage(), e);
            throw new IllegalArgumentException("获取公钥错误");
        }
    }

    /**
     * 私钥加密
     *
     * @param content 加密前的明文数据
     * @param priKey  私钥
     * @return 加密后的Base64字符串
     */
    public String encodeByPriKey(String content, String priKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateKey = getPrivateKey(priKey);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] enSign = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return toHexString(enSign);
        } catch (Exception e) {
            log.error("私钥加密错误" + e.getLocalizedMessage(), e);
            throw new IllegalArgumentException("私钥加密错误");
        }
    }

    /**
     * 获取 私钥
     *
     * @param priKey 私钥
     * @return 私钥
     */
    public PrivateKey getPrivateKey(String priKey) {
        try {
            byte[] privateKeyBytes = toBytes(priKey);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            log.error("获取公钥错误" + e.getLocalizedMessage(), e);
            throw new IllegalArgumentException("获取公钥错误");
        }
    }

    /**
     * 公钥解密
     *
     * @param base64 待解密的Base64数据
     * @param pubKey 公钥
     * @return 解密后的明文数据
     */
    public String decodeByPubKey(String base64, String pubKey) {
        try {
            byte[] publicKeyBytes = toBytes(pubKey);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] design = cipher.doFinal(toBytes(base64));
            return new String(design, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("公钥解密错误" + e.getLocalizedMessage(), e);
            throw new IllegalArgumentException("公钥解密错误");
        }
    }

    /**
     * 私钥解密
     *
     * @param base64 待解密的Base64数据
     * @param priKey 私钥
     * @return 解密后的明文数据
     */
    public String decodeByPriKey(String base64, String priKey) {
        try {
            byte[] privateKeyBytes = toBytes(priKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] design = cipher.doFinal(toBytes(base64));
            return new String(design, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("私钥解密错误" + e.getLocalizedMessage(), e);
            throw new IllegalArgumentException("私钥解密错误");
        }
    }

    /**
     * RSA签名
     *
     * @param content 待签名的明文数据
     * @param priKey  私钥
     * @return 签名
     */
    public String sign(String content, String priKey) {
        try {
            byte[] privateKeyBytes = toBytes(priKey);
            // 取得私钥
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            // 生成私钥
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            // 实例化Signature
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            // 初始化Signature
            signature.initSign(privateKey);
            // 更新
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return toHexString(signature.sign());
        } catch (Exception e) {
            log.error("RSA签名错误" + e.getLocalizedMessage(), e);
            throw new IllegalArgumentException("RSA签名错误");
        }
    }

    /**
     * RSA验签
     *
     * @param content 待校验的明文数据
     * @param sign    数字签名
     * @param pubKey  公钥
     * @return true成功 false失败
     */
    public boolean verify(String content, String sign, String pubKey) {
        try {
            byte[] publicKeyBytes = toBytes(pubKey);
            // 实例化密钥工厂
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            // 初始化公钥
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            // 产生公钥
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            // 实例化Signature
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            // 初始化Signature
            signature.initVerify(publicKey);
            // 更新
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            // 验证
            return signature.verify(toBytes(sign));
        } catch (Exception e) {
            log.error("RSA验签错误" + e.getLocalizedMessage(), e);
            throw new IllegalArgumentException("RSA验签错误");
        }
    }

    /**
     * 转成字符串
     *
     * @param bytes 字节数组
     * @return Base64数据
     */
    public String toHexString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 转成字节数组
     *
     * @param base64 Base64数据
     * @return 字节数组
     */
    public byte[] toBytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}