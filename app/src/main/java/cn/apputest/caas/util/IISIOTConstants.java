/**
 * 
 */
package cn.apputest.caas.util;

import cn.apputest.caas.extinterface.iisiot.staticinfo.SourceUrlRequestUtil;

/** 天津标准信源的接口参数
 * 
 * @author andy_hou
 * @date 2015年10月15日
 * 
 */
public class IISIOTConstants {
	public static String GET_SOURCE_URL = "http://192.168.1.88:8082/iis-rcns/rcns/rcisurl/byTagCode";
	public static String SOURCE_URL= SourceUrlRequestUtil.getInstance().getUrl();
	public static String USER_NAME="zhongxin";
	public static String USER_SECRET_KEY ="fa3b70e069639063e338682337e663c9";//用户的Secret Key
	public static String GET_ALL_BASESTATIONS_URL = SOURCE_URL + "/rcis/Basestations";
	public static String GET_BASESTATION_URL = SOURCE_URL + "/rcis/Basestations/Basestation";
	public static String GET_VEHICLE_APPROVE_QUALITY_URL = SOURCE_URL + "/rcis/VehicleInfo/ApprovedQuality";
	public static String GET_VEHICLE_PLATE_NUMBER_URL = SOURCE_URL + "/rcis/VehicleInfo/PlateNo";
	public static String GET_VEHICLE_VIN_URL = SOURCE_URL + "/rcis/VehicleInfo/VIN";
	public static String CONFIG_REPORT_PERIOD_URL = SOURCE_URL + "/rcis/subscribe/ReportPeriod/config";//4.3.1 数据订阅_上报周期配置接口
	public static String QUERY_REPORT_PERIOD_URL = SOURCE_URL + "/rcis/subscribe/ReportPeriod";//4.3.2 数据订阅_上报周期查询接口s
	public static String CONFIG_TIMESCOPE_URL = SOURCE_URL + "/rcis/subscribe/TimeScope/config";//4.3.3	数据订阅_起止时间配置接口
	public static String QUERY_TIMESCOPE_URL = SOURCE_URL + "/rcis/subscribe/TimeScope";//4.3.4	数据订阅_起止时间查询接口
	public static String START_REPORT_SUBSCRIBE_URL = SOURCE_URL + "/rcis/subscribe/start";//4.3.5 数据订阅_启动订阅接口
	public static String STOP_REPORT_SUBSCRIBE_URL = SOURCE_URL + "/rcis/subscribe/stop";//4.3.6 数据订阅_停止订阅接口
	public static String QUERY_REPORT_SUBSCRIBE_STATUS_URL = SOURCE_URL + "/rcis/subscribe/status";//4.3.7 数据订阅_查询订阅状态接口
	public static String GET_VEHICLE_UNLADEN_MASS_URL = SOURCE_URL+"/rcis/VehicleInfo/unladenMass";//根据标签编码或VIN获取车辆的整备质量
	public static String GET_VEHICLE_TRACTION_MASS_URL = SOURCE_URL+"/rcis/VehicleInfo/tractionMass";
}
