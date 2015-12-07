/**
 * 
 */
package cn.apputest.caas.extinterface.iisiot.staticinfo;

import cn.apputest.caas.util.IISIOTConstants;

/**
 *  获取请求的URL
 * @author zh
 *
 */
public class SourceUrlRequestUtil {
	
	private static SourceUrlRequestUtil  instance;
	private String url;
	private SourceUrlRequestUtil(){	
		url = "http://192.168.1.88:8082/iis-rcis";	//TODO
	}
	public static synchronized SourceUrlRequestUtil getInstance(){
		if(instance == null){
			instance =  new SourceUrlRequestUtil();
		}	
		return instance;
	}
	
	public String getUrl(){
		  return url;
	}
	
}
