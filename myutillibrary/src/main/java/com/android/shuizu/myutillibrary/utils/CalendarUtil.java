package com.android.shuizu.myutillibrary.utils;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2017/9/17/017.
 */

public class CalendarUtil {
    public static final String STANDARD = "yyyy-MM-dd HH:mm:ss";
    public static final String STANDARD_ = "yyyy-MM-dd\nHH:mm:ss";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String HH_mm = "HH:mm";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YY_MM_DD_HH_MM = "yy/MM/dd\nHH:mm";
    public static final String MM_DD_HH_MM = "MM/dd HH:mm";
    /**
     * 标准的
     */
    private Time time;
    private Calendar c;

    public CalendarUtil() {
        c = Calendar.getInstance();
    }

    public CalendarUtil(long time, boolean isSecond) {
        this();
        c.setTimeInMillis(isSecond ? time * 1000 : time);
    }

    public CalendarUtil(long time) {
        this();
        c.setTimeInMillis(time);
    }

    public CalendarUtil(int field, int value) {
        c.set(field, value);
    }

    public CalendarUtil(int year, int month, int date) {
        this();
        c.set(year, month, date);
    }

    public CalendarUtil(int year, int month, int date, int hourOfDay, int minute) {
        this();
        c.set(year, month, date, hourOfDay, minute);
    }

    public CalendarUtil(int year, int month, int date, int hourOfDay, int minute, int second) {
        this();
        c.set(year, month, date, hourOfDay, minute, second);
    }

    public CalendarUtil(String dateStr) {
        this();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
    }

    public void set(int year, int month, int day) {
        c.set(year, month, day);
    }

    public void set(int year, int month, int day, int hourOfDay, int minute) {
        c.set(year, month, day, hourOfDay, minute);
    }

    public void set(int year, int month, int day, int hourOfDay, int minute, int second) {
        c.set(year, month, day, hourOfDay, minute, second);
    }

    public int getYear() {
        return c.get(Calendar.YEAR);
    }

    public void setYear(int year) {
        c.set(Calendar.YEAR, year);
    }

    public int getMonth() {
        return c.get(Calendar.MONTH) + 1;
    }

    public void setMonth(int month) {
        c.set(Calendar.MONTH, month);
    }

    public int getDate() {
        return c.get(Calendar.DATE);
    }

    public void setDate(int date) {
        c.set(Calendar.DATE, date);
    }

    public int getHour() {
        return c.get(Calendar.HOUR);
    }

    public void setHour(int hour) {
        c.set(Calendar.HOUR, hour);
    }

    public int getHourOfDay() {
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return c.get(Calendar.MINUTE);
    }

    public void setMinute(int minute) {
        c.set(Calendar.MINUTE, minute);
    }

    public int getSecond() {
        return c.get(Calendar.SECOND);
    }

    public void setSecond(int second) {
        c.set(Calendar.SECOND, second);
    }

    public long getTimeInMillis() {
        return c.getTimeInMillis();
    }

    public void setTimeInMillis(long timeInMillis) {
        c.setTimeInMillis(timeInMillis);
    }

    public String format(String format, String yearFlag, String hourFlag) {
        String f = format.replace("-", yearFlag).replace(":", hourFlag);
        return format(f);
    }

    public String format(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(c.getTime());
    }

    public int getMillisecond() {
        return c.get(Calendar.MILLISECOND);
    }

    public int getDateOfMonth() {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public int getDayOfWeekInMonth() {
        return c.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    public int getWeekOfMonth() {
        return c.get(Calendar.WEEK_OF_MONTH);
    }

    public int getDayOfWeek() {
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return 0;
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
            default:
                return 0;
        }
    }

    public Calendar getC() {
        return c;
    }

    public void setC(Calendar c) {
        this.c = c;
    }

    /**
     * 获取与当前时间的时间差
     */
    public Long getTimeDifferFromNow(DifferType type) {
        Long now = new CalendarUtil().getTimeInMillis();
        Long tag = getTimeInMillis();
        Long differ = Math.abs(now - tag);
        if (type == DifferType.TYPE_DAY) {
            return differ / (1000 * 3600 * 24);
        } else if (type == DifferType.TYPE_HOUR) {
            return differ / (1000 * 3600);
        } else if (type == DifferType.TYPE_MIN) {
            return differ / (1000 * 60);
        } else if (type == DifferType.TYPE_SEC) {
            return differ / (1000);
        } else {
            return differ;
        }
    }

}

enum DifferType {
    TYPE_DAY, TYPE_HOUR, TYPE_MIN, TYPE_SEC
}
