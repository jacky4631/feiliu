package com.jiam365.modules.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

public class SimpleDateUtils
{
    public static String[] getRecentMonths(int count)
    {
        Calendar calendar = Calendar.getInstance();
        String[] ret = new String[count];
        for (int i = count; i > 0; i--)
        {
            int month = calendar.get(2) + 1;
            String sMonth;
            if (month > 9) {
                sMonth = String.valueOf(month);
            } else {
                sMonth = "0" + month;
            }
            ret[(i - 1)] = (calendar.get(1) + sMonth);
            calendar.add(2, -1);
        }
        return ret;
    }

    public static String getCurMonth()
    {
        return getRecentMonths(1)[0];
    }

    public static String getPastMonth()
    {
        return getRecentMonths(2)[0];
    }

    public static String get2MonthAgo()
    {
        return getRecentMonths(3)[0];
    }

    public static Date[] getMonthPeriod(String month)
    {
        Date date;
        try
        {
            date = DateUtils.parseDate(month, new String[] { "yyyyMM" });
        }
        catch (ParseException e)
        {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, calendar.getActualMaximum(5));
        setDayEnd(calendar);
        return new Date[] { date, calendar.getTime() };
    }

    public static Date[] getMonthPeriod()
    {
        return new Date[] { getFirstDayOfCurMonth(), getLastDayOfCurMonth() };
    }

    public static Date[] getWeekPeriod()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(2);
        calendar.set(7, 2);
        setDayStart(calendar);
        calendar.set(14, 0);
        Date d1 = calendar.getTime();

        calendar.set(7, 1);
        setDayEnd(calendar);
        Date d2 = calendar.getTime();
        return new Date[] { d1, d2 };
    }

    public static Date getDateStart(Date d)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        setDayStart(calendar);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static Date getDateEnd(Date d)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        setDayEnd(calendar);
        return calendar.getTime();
    }

    public static Date[] getDayPeriod(int offset)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(6, offset);

        setDayStart(calendar);

        Date d1 = calendar.getTime();
        setDayEnd(calendar);
        Date d2 = calendar.getTime();
        return new Date[] { d1, d2 };
    }

    public static Date[] getTodayPeriod()
    {
        Calendar calendar = Calendar.getInstance();
        setDayStart(calendar);

        Date d1 = calendar.getTime();
        setDayEnd(calendar);
        Date d2 = calendar.getTime();
        return new Date[] { d1, d2 };
    }

    public static boolean isRemainDays(int remainDays)
    {
        Calendar ca = Calendar.getInstance();
        int today = ca.get(5);
        ca.set(5, ca.getActualMaximum(5));
        int lastDay = ca.get(5);
        return lastDay - today < remainDays;
    }

    public static Date getFirstDayOfCurMonth()
    {
        Calendar c = Calendar.getInstance();
        c.set(5, 1);
        setDayStart(c);
        c.set(14, 0);
        return c.getTime();
    }

    public static Date getLastDayOfxMonthAgo(int month)
    {
        Calendar c = Calendar.getInstance();
        c.add(2, -month);
        c.set(5, c.getActualMaximum(5));
        setDayEnd(c);
        c.set(14, 0);
        return c.getTime();
    }

    public static Date getLastDayOfCurMonth()
    {
        Calendar c = Calendar.getInstance();
        c.set(5, c.getActualMaximum(5));
        setDayEnd(c);
        return c.getTime();
    }

    private static void setDayEnd(Calendar calendar)
    {
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        calendar.set(14, 999);
    }

    private static void setDayStart(Calendar calendar)
    {
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
    }

    public static Date safeParseDate(String sDate, String pattern)
    {
        Date date = null;
        try
        {
            date = DateUtils.parseDate(sDate, new String[] { pattern });
        }
        catch (ParseException localParseException) {}
        return date;
    }
}
