package com.smart4y.cloud.core.infrastructure.toolkit.base;

import lombok.extern.apachecommons.CommonsLog;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

/**
 * 时间工具类
 *
 * @author Youtao
 *         Created by youtao on 2018/4/23.
 */
@CommonsLog
@SuppressWarnings("unused")
public class DateHelper extends org.apache.commons.lang3.time.DateUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 时间毫秒数 转为 日期
     *
     * @param milliseconds 时间毫秒数（13位毫秒值）
     * @return 日期
     */
    public static LocalDate milliseconds2Date(long milliseconds) {
        return LocalDateTime
                .ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * 月的第一天
     *
     * @param date 日期
     * @return 月份的第一天
     */
    public static LocalDate firstDayOfMonth(LocalDate date) {
        return LocalDate.of(date.getYear(), date.getMonth(), 1);
    }

    /**
     * 月的最后一天
     *
     * @param date 日期
     * @return 月份的最后一天
     */
    public static LocalDate lastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 周的第一点
     * <p>
     * 每周的第一天为 Monday
     * </p>
     *
     * @param date 日期
     * @return 周的第一天
     */
    public static LocalDate firstDayOfWeek(LocalDate date) {
        TemporalField temporalField = WeekFields.of(DayOfWeek.MONDAY, 1).dayOfWeek();
        return date.with(temporalField, 1);
    }

    /**
     * 周的最后一天
     * <p>
     * 每周的最后一天为 Sunday
     * </p>
     *
     * @param date 日期
     * @return 周的最后一天
     */
    public static LocalDate lastDayOfWeek(LocalDate date) {
        TemporalField temporalField = WeekFields.of(DayOfWeek.MONDAY, 1).dayOfWeek();
        return date.with(temporalField, 7);
    }

    /**
     * 获取 时间戳
     *
     * @return 时间戳（格式：yyyyMMddHHmmss）
     */
    public static long getCurrentTimestamp() {
        return Long.parseLong(getCurrentTimestampStr());
    }

    /**
     * 获取当前时间戳（yyyyMMddHHmmss）
     */
    public static String getCurrentTimestampStr() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}