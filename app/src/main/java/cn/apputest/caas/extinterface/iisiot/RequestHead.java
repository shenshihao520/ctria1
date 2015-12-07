/**
 * 
 */
package cn.apputest.caas.extinterface.iisiot;

/**  天津對接接口請求頭結構
 * 
 * @author andy_hou
 * @date 2015年10月15日
 * 
 */
public class RequestHead {

private String userName;
private String requestDatetime;
private String signature;


public RequestHead(String userName, String requestDatetime, String signature) {
	super();
	this.userName = userName;
	this.requestDatetime = requestDatetime;
	this.signature = signature;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getRequestDatetime() {
	return requestDatetime;
}
public void setRequestDatetime(String requestDatetime) {
	this.requestDatetime = requestDatetime;
}
public String getSignature() {
	return signature;
}
public void setSignature(String signature) {
	this.signature = signature;
}



}
