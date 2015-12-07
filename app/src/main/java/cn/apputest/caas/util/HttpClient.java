package cn.apputest.caas.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
	 //车辆数据传输测试
	 private static final String URL = "http://192.168.0.5:8081/caps/api/reportEvent/upload.do";
	 //根据VIN获取车牌信息
	 private static final String CAR_URL = "http://111.198.72.101:8081/ctea/api/plateNumberByVin";
	 
	 private static final int OpenTimeout = 5000;//超时时间

	 /**
	  * 请求URL
	  * @param url 需要请求的URL
	  * @param data 请求数据
	  * @param contenttype 请求类型
	  * @param encoding 编码
	  * @return 返回值
	  * @throws Exception 异常捕获
	  */
	 public static String UrlOpen(String url,String data,String contenttype,String encoding) throws Exception {
		 	try{
		 		URL weburl = new URL(url);
		 		HttpURLConnection conn = (HttpURLConnection) weburl.openConnection();
		 		conn.setRequestProperty("Content-Type", contenttype); 
		 		conn.setRequestMethod("POST");// 提交模式
		 		conn.setConnectTimeout(HttpClient.OpenTimeout);//连接超时 单位毫秒
		 		conn.setReadTimeout(HttpClient.OpenTimeout);//读取超时 单位毫秒
		 		if(data!=null && data.trim().length()!=0){
		 			conn.setDoOutput(true);// 是否输入参数
		 			byte[] bypes = data.toString().getBytes(encoding);//设置参数编码
		 			conn.getOutputStream().write(bypes);// 输入参数
		 		}
		 		InputStream inStream=conn.getInputStream();
		 		String result = new String(readInputStream(inStream), encoding);//输出参数
		 		return result;
		 	}catch (Exception e) {
				throw e;
			}
	  }
	 /**
	  * 请求URL
	  * @param url 需要请求的URL
	  * @param contenttype 请求类型
	  * @param encoding 编码
	  * @return 返回值
	  * @throws Exception 异常捕获
	  */
	 private static String UrlGet(String url,String contenttype,String encoding) throws Exception {
		 	try{
		 		URL weburl = new URL(url);
		 		HttpURLConnection conn = (HttpURLConnection) weburl.openConnection();
		 		conn.setRequestProperty("Content-Type", contenttype); 
		 		conn.setRequestMethod("GET");// 提交模式
		 		conn.setConnectTimeout(HttpClient.OpenTimeout);//连接超时 单位毫秒
		 		conn.setReadTimeout(HttpClient.OpenTimeout);//读取超时 单位毫秒
		 		InputStream inStream=conn.getInputStream();
		 		String result = new String(readInputStream(inStream), encoding);//输出参数
		 		return result;
		 	}catch (Exception e) {
				throw e;
			}
	  }
	 
	  private static byte[] readInputStream(InputStream inStream) throws Exception{
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len = 0;
	        while( (len = inStream.read(buffer)) !=-1 ){
	            outStream.write(buffer, 0, len);
	        }
	        byte[] data = outStream.toByteArray();//网页的二进制数据
	        outStream.close();
	        inStream.close();
	        return data;
	  }
    /**
     * 发送信息测试
     * @param phone
     * @param msg
     */
    public static void sendInfo(StringBuilder json){
    	try{
    	 String contenttype = "text/html";
		 String encoding = "utf-8";
		 HttpClient.UrlOpen(HttpClient.URL,json.toString(),contenttype,encoding);
    	}catch (Exception e) {}
    }
    /**
     * 获取车牌号
     * @param vin
     * @return
     */
    public static String getPLATE_NUMBER(String vin){
    	String PLATE_NUMBER = null;
    	try{
       	 //String contenttype = "application/x-www-form-urlencoded";
       	 String contenttype = "application/x-www-form-urlencoded";
   		 String encoding = "utf-8";
   		 PLATE_NUMBER =  HttpClient.UrlGet(HttpClient.CAR_URL+"/"+vin,contenttype,encoding);
       	}catch (Exception e) {}
    	return PLATE_NUMBER;
    }
    /**
     * 发送信息测试
     * @param phone
     * @param msg
     */
    public static void sendInfotest(){
    	try{
    	 //String contenttype = "text/html";
    	String contenttype = "application/x-www-form-urlencoded";
		 String encoding = "utf-8";
		 String url = "http://192.168.1.145:8080/caps/api/ctgmEvent/upload.do";
		 HttpClient.UrlOpen(url,"{'ID':233}",contenttype,encoding);
    	}catch (Exception e) {}
    }
}
