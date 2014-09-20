package net.dolpen.gae.libs.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {


    /**
     * RFC1123形式に時刻を変換する。
     */
    private static String toRFC1123Format(long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"), Locale.UK);
        calendar.setTimeInMillis(time);
        return String.format(java.util.Locale.UK, "%1$ta, %1$te %1$tb %1$tY %1$tH:%1$tM:%1$tS GMT", calendar);
    }

    /**
     * RFC1123形式に時刻を変換する。
     */
    public static String toRFC1123Format(Date date, long diff) {
        return toRFC1123Format(date.getTime() + diff);
    }

}
