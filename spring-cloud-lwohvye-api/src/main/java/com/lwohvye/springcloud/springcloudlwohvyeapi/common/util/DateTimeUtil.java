package com.lwohvye.springcloud.springcloudlwohvyeapi.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 时间相关工具类
 */
public class DateTimeUtil {

    private DateTimeUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final String AREA_NAME = "Asia/Shanghai";

    /**
     * 获取当前格式化日期时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurFormatTime() {
//      获取本时区当前时间
        var now = Instant.now().atZone(ZoneId.of(AREA_NAME));
//      获取格式化时间
        var localDateTime = now.toLocalDateTime();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    /**
     * 获取当前格式化日期
     *
     * @return yyyy-MM-dd
     */
    public static String getCurFormatDate() {
//      获取本时区当前时间
        var now = Instant.now().atZone(ZoneId.of(AREA_NAME));
//      获取格式化时间
        var localDateTime = now.toLocalDateTime();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }

    /**
     * 获取当前时间毫秒值
     *
     * @return
     */
    public static long getCurMilli() {
        return LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 获取对应天前凌晨对应的毫秒值
     *
     * @param days
     * @return
     */
    public static long minusDayMin(int days) {
        return LocalDateTime.of(LocalDate.now().minusDays(days), LocalTime.MIN).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 获取对应天前深夜对应的毫秒值
     *
     * @param days
     * @return
     */
    public static long minusDayMax(int days) {
        return LocalDateTime.of(LocalDate.now().minusDays(days), LocalTime.MAX).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 获取对应小时前对应的毫秒值
     *
     * @param hours
     * @return
     */
    public static long minusHoursMilli(int hours) {
        return LocalDateTime.now().minusHours(hours).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 获取对应分钟前对应的毫秒值
     *
     * @param minutes
     * @return
     */
    public static long minusMinutesMilli(int minutes) {
        return LocalDateTime.now().minusMinutes(minutes).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 根据毫秒值获取对应格式化时间
     *
     * @param milli
     * @return
     */
    public static String formatMillis(long milli) {
        var instant = Instant.ofEpochMilli(milli);
        var zoneId = ZoneId.of(AREA_NAME);
        var localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
