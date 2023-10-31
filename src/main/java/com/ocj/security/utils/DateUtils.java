package com.ocj.security.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final String DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss";

    private static final String DEFAULT_PAY_DATE = "yyyyMMddHHmmss";

    public static final String COURSE_DATE="yyyy-MM-dd";

    public static Date stringToDate(String dateStr, String pattern)  {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 增加时间
     * @param date 时间
     * @param timeLong 增加的长度
     * @return 返回一个Date
     */

    public static Date dateAddTime(Date date,Integer timeLong){

        // 创建一个 Calendar 对象并设置为给定 Date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 在 Calendar 上添加24小时
        calendar.add(Calendar.HOUR_OF_DAY, timeLong);

        // 获取新的 Date 对象
       return calendar.getTime();
    }

    public static String dateToString(Date date,String pattern){

        DateFormat dateFormat = new SimpleDateFormat(pattern);

        return dateFormat.format(date);
    }

    public static String dateToString(Date date){

        return dateToString(date,DEFAULT_PAY_DATE);
    }
    public static Date stringToDate(String dateStr) {
        return stringToDate(dateStr, DEFAULT_PATTERN);
    }



}
