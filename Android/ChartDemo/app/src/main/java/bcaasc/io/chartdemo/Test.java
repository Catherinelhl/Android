package bcaasc.io.chartdemo;

import bcaasc.io.chartdemo.tool.DateFormatTool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.text.format.Time.TIMEZONE_UTC;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/6
 */
public class Test {

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        System.out.println("----:"+sdf.format(getCurrentYearStartTime()));
        System.out.println(sdf.format(getStartTime()));
        System.out.println(sdf.format(getEndTime()));
        System.out.println(DateFormatTool.getUTCDateForAMPMFormat2(String.valueOf(getBeginDayOfYesterday().getTime())));
        System.out.println(DateFormatTool.getUTCDateForAMPMFormat2(String.valueOf(getEndDayOfYesterday().getTime())));
        System.out.println("---"+DateFormatTool.getUTCDateForAMPMFormat2(String.valueOf(getCurrentYearStartTime().getTime())));
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
    public static Date getBeginDayOfYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getStartTime());
//        cal.add(Calendar.DAY_OF_MONTH, -90);
        cal.add(Calendar.YEAR, -1);
        System.out.println(DateFormatTool.getUTCDateForAMPMFormat2(String.valueOf(cal.getTimeInMillis())));
        return cal.getTime();
    }

    //获取昨天的结束时间
    public static Date getEndDayOfYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getEndTime());
//        cal.add(Calendar.DAY_OF_MONTH, -90);
        cal.add(Calendar.YEAR, -1);
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
        }
        return now;
    }

}
