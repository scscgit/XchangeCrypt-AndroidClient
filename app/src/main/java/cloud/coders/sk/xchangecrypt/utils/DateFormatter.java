package cloud.coders.sk.xchangecrypt.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Peter on 22.04.2018.
 */
public class DateFormatter {
    public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String FORMAT_dd_MMM_yy = "dd MMM yy";
    public static final String FORMAT_dd_MMM = "dd MMM";
    public static final String FORMAT_yyyy = "yyyy";
    public static final String FORMAT_YYYYMMDD_HHMMSS = "yyyyMMdd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HHMM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DD_MM_YYYY_HH_MM = "dd. MM. yyyy HH:mm";
    public static final String FORMAT_DD_MM_YYYY_HH_MM_SS = "dd.MM.yyyy HH:mm:ss";
    public static final String FORMAT_DD_MM_YYYY__HH_MM_SS = "dd-MM-yyyy-HH-mm-ss";
    public static final String FORMAT_DD_MM_YYYY = "dd.MM.yyyy";
    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_HH_MM_SS = "HH:mm:ss";
    public static final String FORMAT_dd_MMM_yyyy = "dd MMM yyyy";

    public static Date getDateFromString(String date, String format) {
        if (date == null)
            return null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getStringFromDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String getStringFromLong(long date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date dateObj = new Date(date);
        return simpleDateFormat.format(dateObj);
    }

    public static final int getMonthsDifference(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int m1 = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
        int m2 = calendar2.get(Calendar.YEAR) * 12 + calendar2.get(Calendar.MONTH);
        return m2 - m1 + 1;
    }
}