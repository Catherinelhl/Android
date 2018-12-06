package bcaasc.io.chartdemo.tool;

import bcaasc.io.chartdemo.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.text.format.Time.TIMEZONE_UTC;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/26
 * <p>
 * 时间转换
 */
public class DateFormatTool {
    private String TAG = DateFormatTool.class.getSimpleName();

    private final static String DATETIMEFORMATWithH = "yy/MM/dd HH";
    private final static String DATETIMEFORMAT = "yy/MM/dd";
    // Coordinated Universal Time
    private final static String TIMEZONE_UTC = "UTC";

    //取得当前时间
    public static String getCurrentTime() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIMEFORMAT);
        Date date = new Date();
        String strDate = simpleDateFormat.format(date);


        return strDate;
    }

    public static String getUTCDateForAMPMFormat(String timeStamp) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIMEFORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        Date date = new Date();
        date.setTime(Long.valueOf(timeStamp));
        String dataAMPM = simpleDateFormat.format(date);
        return dataAMPM;
    }

    public static String getUTCDateForAMPMFormat2(String timeStamp) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIMEFORMATWithH);
//        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        Date date = new Date();
        date.setTime(Long.valueOf(timeStamp));
        String dataAMPM = simpleDateFormat.format(date);


        return dataAMPM;
    }


    private static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    private static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    //获取昨天的开始时间
    public static Date getPastTimeOfStartByCycleTime(Constants.CycleTime cycleTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getStartTime());
        switch (cycleTime) {
            case oneMonth:
                cal.add(Calendar.MONTH, -1);
                break;
            case threeMonth:
                cal.add(Calendar.MONTH, -3);
                break;
            case YTD:
                cal.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case oneYear:
                cal.add(Calendar.YEAR, -1);
                break;
        }
        System.out.println(DateFormatTool.getUTCDateForAMPMFormat2(String.valueOf(cal.getTimeInMillis())));
        return cal.getTime();
    }

    //获取昨天的结束时间
    public static Date getPastTimeOfEndByCycleTime(Constants.CycleTime cycleTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getEndTime());
        switch (cycleTime) {
            case oneMonth:
                cal.add(Calendar.MONTH, -1);
                break;
            case threeMonth:
                cal.add(Calendar.MONTH, -3);
                break;
            case YTD:
                cal.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case oneYear:
                cal.add(Calendar.YEAR, -1);
                break;
        }
        System.out.println(DateFormatTool.getUTCDateForAMPMFormat2(String.valueOf(cal.getTimeInMillis())));
        return cal.getTime();
    }


    /**
     * 当前年的开始时间
     *
     * @return
     */
    public static Date getCurrentYearStartTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        Date now = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            LogTool.e(DateFormatTool.class.getSimpleName(), e.getCause().toString());
        }
        return now;
    }

}
