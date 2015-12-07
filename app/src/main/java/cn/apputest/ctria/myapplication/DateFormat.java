package cn.apputest.ctria.myapplication;

import java.text.SimpleDateFormat;

/**
 * @author Shenshihao 时间日期格式类
 */
public class DateFormat {
    String Date;
    public static String FORMAT3 = "yyyy-MM-dd HH:mm:ss";

    // 上传记录 的时间格式
    public String getDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM/dd hh:mm");

        Date = sDateFormat.format(new java.util.Date());
        return Date;
    }

    public String getDate2() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date = sDateFormat.format(new java.util.Date());
        return Date;
    }

    public String getDate3() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date = sDateFormat.format(new java.util.Date());
        return Date;
    }

    public static String getDateString(String formateString) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(formateString);
        String str = sDateFormat.format(new java.util.Date());
        return str;
    }
}
