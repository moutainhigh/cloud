package com.smart4y.cloud.core.infrastructure.toolkit.random;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * 随机数工具类
 *
 * @author Youtao
 *         Created by youtao on 2017/12/7.
 */
public enum RandomHelper {
    INSTANCE;

    /**
     * 随机数生成器
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static String[] CHARS = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 生成 uuid
     * <p>
     * 格式：cd4aabd9a2a34f429966d029040f227f
     * </p>
     *
     * @return UUID
     */
    public String uuid() {
        return this.uuid(Boolean.FALSE);
    }

    /**
     * 生成 uuid
     * <p>
     * 格式：9f5db724-2aaf-449b-9c8a-c18e6d13e9ae
     * </p>
     *
     * @return uuid
     */
    public String prettyUuid() {
        return this.uuid(Boolean.TRUE);
    }

    /**
     * 生成 UUID
     * <p>
     * 格式：fhkz8QmR
     * </p>
     *
     * @return UUID
     */
    public String shortUuid() {
        String uuid = uuid();
        return shortUuid(uuid);
    }

    /**
     * 生成 UUID
     * <p>
     * 格式：fhkz8QmR
     * </p>
     *
     * @param uuid 32位字符串
     * @return UUID
     */
    public String shortUuid(String uuid) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int xxx = i * 4;
            String str = uuid.substring(xxx, xxx + 4);
            int x = Integer.parseInt(str, 16);
            builder.append(CHARS[x % 0x3E]);
        }
        return builder.toString();
    }

    private String uuid(boolean isPretty) {
        String uuid = UUID.randomUUID().toString();
        return isPretty ? uuid : uuid.replace("-", "");
    }

    /**
     * 生成 19位Long
     * <p>
     * 使用 SecureRandom 随机
     * 格式：8918950405711301062
     * </p>
     *
     * @return the long
     */
    public long randLong() {
        long number = SECURE_RANDOM.nextLong();
        if (Long.MIN_VALUE == number) {
            return Long.MAX_VALUE;
        } else {
            return Math.abs(number);
        }
    }

    /**
     * 生成 指定位数数字
     * <p>
     * 例如：{@code charCount} 8，则值为：87691909, 27481737
     * </p>
     *
     * @param charCount 位数
     * @return String rand num
     */
    public String randNum(int charCount) {
        StringBuilder charValue = new StringBuilder();
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randInt(0, 10) + '0');
            charValue.append(String.valueOf(c));
        }
        return charValue.toString();
    }

    /**
     * 生成 范围内的随机数字
     * <p>
     * 位数不定
     * 例如：{@code from} 1 - {@code to} 5000，则值为：2, 12, 600...
     * </p>
     *
     * @param from 开始
     * @param to   结束
     * @return int int
     */
    public int randInt(int from, int to) {
        return from + new Random().nextInt(to - from);
    }
}