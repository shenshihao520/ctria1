package cn.apputest.ctria.service;

/**
 * 
 * @author Shihao Shen
 * 一些常量  用于控制时间及serivce
 *
 */
public class Constants {

    public static final int ELAPSED_TIME_F =60* 1000;
    public static final int ELAPSED_TIME_D =60* 1000;
    public static final int RETRIVE_SERVICE_COUNT = 50;
    public static final int ELAPSED_TIME_DELAY_F = 2*60*1000;//失败重传delay
    public static final int ELAPSED_TIME_DELAY_D = 2*60*1000;//数据更新delay
    public static final int BROADCAST_ELAPSED_TIME_DELAY = 2*60*1000;
    public static final String POI_SERVICE = "cn.apputest.ctria.UploadFailRecordService";
    public static final String POI_SERVICE_ACTION = "cn.apputest.ctria.UploadFailRecordService.action";
    public static final String POI_SERVICE_updatedata = "cn.apputest.ctria.UpdateDataService";
    public static final String POI_SERVICE_ACTION_updatedata = "cn.apputest.ctria.UpdateDataService.action";
    public static int TIMR_OUT = 5000;
}
