/**
 * 
 */
package cn.apputest.caas.extinterface.iisiot;

import org.json.JSONException;


/**
 * 
 * @author andy_hou
 * @date 2015年10月15日
 * 
 */
public interface IRequst {	
	
	int TAG_CODE_TYPE = 1;
	int VIN_TYPE = 2;
	
	/*
	 * 執行并處理，可以實現對外調用
	 */
	RequestResult execute();
	/*
	 * 創建除RequestHead請求體
	 */
	void buildRequestBody() throws JSONException;
	void sendPostRequest()  throws Exception;
	/*
	 * 
	 */
	Object postHandle() throws JSONException;
}
