/**
 * 
 */
package cn.apputest.caas.extinterface.iisiot;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.R.integer;
import android.util.Log;


import cn.apputest.caas.util.HttpClient;
import cn.apputest.caas.util.IISIOTConstants;
import cn.apputest.ctria.myapplication.DateFormat;
import cn.apputest.ctria.myapplication.MD5;

/**
 * 
 * @author andy_hou
 * @date 2015年10月15日
 * 
 */
public abstract class AbstractRequest implements IRequst {

	protected String requestURL;
	protected JSONObject jsonResult;
	protected JSONObject postData = new JSONObject();

	public AbstractRequest(String requestURL) {
		super();
		this.requestURL = requestURL;
	}

	public RequestResult execute() {
		RequestResult result = new RequestResult();
		System.out.println("requestURL:" + requestURL);
		try {
			sendPostRequest();
			Object obj = postHandle();
			result.setResult(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			printAllErrMsg(e);
			result.setErrorCode(1);
			result.setResult(e.getMessage());

		}
		return result;

	}

	public void sendPostRequest() throws Exception {

		//String requestHead = buildRequestHeadStr();
		
		//postData.put("requestHead", requestHead);
		buildRequestBody();
		try {
			String requestHead = buildRequestHeadStr();
			StringBuilder json = new StringBuilder("{\"requestHead\":"+requestHead);
			Iterator<String> iter = postData.keys();
			while (iter.hasNext()) {
				String keyName = iter.next();
				Object value = postData.get(keyName);
				if(value instanceof integer){
					json.append(",\""+keyName+"\":"+value+"");
				}
				else{
					json.append(",\""+keyName+"\":\""+value+"\"");
				}
			}
			json.append("}");
			System.out.println("json:"+json.toString());
			String  result = HttpClient.UrlOpen(requestURL,json.toString(),"text/html","utf-8");
			Log.i("http", result);
			
			
			if (StringUtils.isNotBlank(result)) {
				jsonResult = new JSONObject(result);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (jsonResult != null) {
			// 返回体：先转string在转json数组然后再转list
			JSONObject responseH = jsonResult.getJSONObject("responseHead");

			ResponseHead responseHead = new ResponseHead();
			responseHead.setResultCode(responseH.getString("resultCode"));
			responseHead.setResultMsg(responseH.getString("resultMsg"));

			if (!"0".equals(responseHead.getResultCode())) {
				throw new Exception("Failure due to business error:"
						+ responseHead.toString());
			}
		}

	}

	private String buildRequestHeadStr() {
		Date now = new Date();
		String requestDatetime = DateFormat.getDateString(DateFormat.FORMAT3);// (now,
																				// DateProcessorUtil.FORMAT_THREE);
		long requestDatetime4Signature = (now.getTime() / 1000) * 1000; // 去掉毫秒数
		String signatureSource = IISIOTConstants.USER_NAME
				+ IISIOTConstants.USER_SECRET_KEY + requestDatetime4Signature;
		String signature = MD5.md5(signatureSource);
		StringBuffer requestHead = new StringBuffer();
//		requestHead.append("requestHead:");
		requestHead.append("{");
		requestHead.append("\"userName\":\"" + IISIOTConstants.USER_NAME+ "\",");
		requestHead.append("\"requestDatetime\":\"" + requestDatetime + "\",");
		requestHead.append("\"signature\":\"" + signature + "\"");
		requestHead.append("}");
		return requestHead.toString();
	}

	private void printAllErrMsg(Throwable e) {

		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			// 将出错的栈信息输出到printWriter中
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}

		System.out.println(sw.toString());

	}

}
