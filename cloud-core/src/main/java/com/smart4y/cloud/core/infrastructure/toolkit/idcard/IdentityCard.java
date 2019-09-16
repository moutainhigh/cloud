package com.smart4y.cloud.core.infrastructure.toolkit.idcard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 身份证信息
 *
 * @author Youtao
 *         Created by youtao on 2018/6/27.
 */
@Getter
@ToString
@AllArgsConstructor
@Accessors(fluent = true)
public class IdentityCard {

    /**
     * 身份证号
     * <p>
     * 18位号码
     * 例如：36232119910218073X
     * </p>
     */
    private final String idNumber;
    /**
     * 省份
     * <p>
     * 11 - 北京
     * 12 - 天津
     * 13 - 河北
     * 14 - 山西
     * 15 - 内蒙古自治区
     * 21 - 辽宁
     * 22 - 吉林
     * 23 - 黑龙江
     * 31 - 上海
     * 32 - 江苏
     * 33 - 浙江
     * 34 - 安徽
     * 35 - 福建
     * 36 - 江西
     * 37 - 山东
     * 41 - 河南
     * 42 - 湖北
     * 43 - 湖南
     * 44 - 广东
     * 45 - 广西壮族自治区
     * 46 - 海南
     * 50 - 重庆
     * 51 - 四川
     * 52 - 贵州
     * 53 - 云南
     * 54 - 西藏自治区
     * 61 - 陕西
     * 62 - 甘肃
     * 63 - 青海
     * 64 - 宁夏回族自治区
     * 65 - 新疆维吾尔族自治区
     * 71 - 台湾
     * 81 - 香港
     * 82 - 澳门
     * 91 - 国外
     * </p>
     * <p>
     * 华北（北京、天津、河北、山西、内蒙古）
     * 东北（辽宁、吉林、黑龙江）
     * 华东（上海、江苏、浙江、江西、安徽、福建、山东）
     * 中南（河南、湖北、湖南、广东、广西、海南）
     * 西南（重庆、四川、贵州、云南、西藏）
     * 西北（陕西、甘肃、青海、宁夏、新疆）
     * 港澳台（香港、澳门、台湾）
     * 国外
     * </p>
     */
    private final String province;
    /**
     * 城市
     */
    private final String city;
    /**
     * 区县
     */
    private final String region;
    /**
     * 年份
     */
    private final int year;
    /**
     * 月份
     */
    private final int month;
    /**
     * 日期
     */
    private final int day;
    /**
     * 性别（男、女）
     */
    private final String gender;
    /**
     * 出生日期
     */
    private final LocalDate birthday;
    /**
     * 年龄（周岁）
     */
    private final int age;
}