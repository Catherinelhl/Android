package com.xxmassdeveloper.mpchartexample.tool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/26
 *
 * 时间转换
 */
public class DateFormatTool {

    private final static String DATETIMEFORMAT = "yy/MM/dd HH";
    // Coordinated Universal Time
    private final static String TIMEZONE_UTC = "UTC";
    //取得当前时间
    public static String getCurrentTime() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIMEFORMAT);
        Date date = new Date();
        String strDate = simpleDateFormat.format(date);

//        strDate = strDate.substring(0, strDate.lastIndexOf("/"));

        return strDate;
    }

    public static String getUTCDateForAMPMFormat(String timeStamp) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIMEFORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        Date date = new Date();
        date.setTime(Long.valueOf(timeStamp));
        String dataAMPM = simpleDateFormat.format(date);
        return dataAMPM;
    }

}
