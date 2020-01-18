package com.wp.android_base.base.utils;


import com.wp.android_base.base.utils.language.LanguageUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wangpeng on 2018/1/6.
 * 时间工具类
 */

public class TimeUtil {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    /**
     * 注意time的单位
     * @param timeSeconds：秒，不是毫秒，注意单位
     * @return
     */
    public static String formatLong2Time(long timeSeconds) {
        return formatLong2Time(timeSeconds, DEFAULT_PATTERN);
    }

    public static String formatLong2Time(long timeSeconds, String pattern) {
        if (TextUtil.isEmpty(pattern)) {
            pattern = DEFAULT_PATTERN;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(timeSeconds * 1000);
    }

    public static String formatLong2Time(long timeSeconds, String pattern, TimeZone timeZone) {
        if (TextUtil.isEmpty(pattern)) {
            pattern = DEFAULT_PATTERN;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern,Locale.getDefault());
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(timeSeconds * 1000);
    }

    public static String formatString2Time(String time) {
        return formatString2Time(time, DEFAULT_PATTERN);
    }

    public static String formatString2Time(String timeSeconds, String pattern) {
        if (TextUtil.isEmpty(pattern)) {
            pattern = DEFAULT_PATTERN;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(Long.parseLong(timeSeconds) * 1000);
    }

    /**
     * 将秒转为天，时，分，秒
     * @param timeSeconds
     * @return
     */
    public static String formatLong2TimeDetail(long timeSeconds) {
        String timeDetail = null;
        long days = timeSeconds / (60 * 60 * 24);
        long hours = (timeSeconds % (60 * 60 * 24)) / (60 * 60);
        long minutes = (timeSeconds % (60 * 60)) / 60;
        long seconds = timeSeconds % 60;

        if (days > 0) {
            timeDetail = days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒" ;
        } else if (hours > 0) {
            timeDetail = hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        } else if (minutes > 0) {
            timeDetail = minutes + "分钟" + seconds + "秒";
        } else {
            timeDetail = seconds + "秒";
        }
        return timeDetail;
    }

    /**
     * 是否今天
     * @param timeSeconds
     * @return
     */
    public static boolean isToday(long timeSeconds) {

        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH);
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        Calendar target = Calendar.getInstance();
        target.setTime(new Date(timeSeconds * 1000));
        int targetYear = target.get(Calendar.YEAR);
        int targetMonth = target.get(Calendar.MONTH);
        int targetDay = target.get(Calendar.DAY_OF_MONTH);

        return currentYear == targetYear && currentMonth == targetMonth && currentDay == targetDay;
    }

    /**
     * 是否昨天
     * @param timeSeconds
     * @return
     */
    public static boolean isYesterday(long timeSeconds) {

        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH);
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        Calendar target = Calendar.getInstance();
        target.setTime(new Date(timeSeconds * 1000));
        int targetYear = target.get(Calendar.YEAR);
        int targetMonth = target.get(Calendar.MONTH);
        int targetDay = target.get(Calendar.DAY_OF_MONTH);

        return currentYear == targetYear && currentMonth == targetMonth && currentDay == targetDay + 1;
    }

    /**
     * 是否前天
     * @param timeSeconds
     * @return
     */
    public static boolean isTheDayBeforeYesterday(long timeSeconds) {

        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH);
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        Calendar target = Calendar.getInstance();
        target.setTime(new Date(timeSeconds * 1000));
        int targetYear = target.get(Calendar.YEAR);
        int targetMonth = target.get(Calendar.MONTH);
        int targetDay = target.get(Calendar.DAY_OF_MONTH);

        return currentYear == targetYear && currentMonth == targetMonth && currentDay == targetDay + 2;
    }
}
