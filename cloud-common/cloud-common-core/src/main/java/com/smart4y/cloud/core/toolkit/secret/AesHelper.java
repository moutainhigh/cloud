package com.smart4y.cloud.core.toolkit.secret;

import lombok.extern.apachecommons.CommonsLog;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Youtao
 *         Created By Youtao on 2017/12/06.
 */
@CommonsLog
public enum AesHelper {
    INSTANCE;

    private static final String DEFAULT_SALT = "smart4y.com";
    private static final String AES = "AES";

    AesHelper() {
    }

    /**
     * 加密
     * 1.构造密钥生成器
     * 2.根据 {@link AesHelper#DEFAULT_SALT} 规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     *
     * @param content 字符串
     * @return 密文
     */
    public String encode(String content) {
        return this.encode(content, DEFAULT_SALT);
    }

    /**
     * 加密
     * 1.构造密钥生成器
     * 2.根据 <code>salt</code> 规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     *
     * @param content 字符串
     * @param salt    密码
     * @return 密文
     */
    public String encode(String content, String salt) {
        try {
            // 1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance(AES);
            // 2.根据 DEFAULT_SALT 规则初始化密钥生成器
            // 生成一个128位的随机源,根据传入的字节数组
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(salt.getBytes(StandardCharsets.UTF_8));
            keygen.init(128, random);
            // 3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            // 4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            // 5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, AES);
            // 6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES);
            // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = content.getBytes(StandardCharsets.UTF_8);
            // 9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
            // 10.将加密后的数据转换为字符串返回
            return Base64.getEncoder().encodeToString(byte_AES);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.根据 {@link AesHelper#DEFAULT_SALT} 将加密内容解密
     *
     * @param content 密文
     * @return 明文
     */
    public String decode(String content) {
        return this.decode(content, DEFAULT_SALT);
    }

    /**
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.根据 <code>salt</code> 将加密内容解密
     *
     * @param content 密文
     * @param salt    密码
     * @return 明文
     */
    public String decode(String content, String salt) {
        try {
            // 1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance(AES);
            // 2.根据 DEFAULT_SALT 规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(salt.getBytes(StandardCharsets.UTF_8));
            keygen.init(128, random);
            // 3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            // 4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            // 5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, AES);
            // 6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES);
            // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 8.将加密并编码后的内容解码成字节数组
            byte[] byte_content = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
            // 解密
            byte[] byte_decode = cipher.doFinal(byte_content);
            return new String(byte_decode, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
            log.error(e.getMessage(), e);
        }
        // 如果有错返回 null
        return null;
    }
}