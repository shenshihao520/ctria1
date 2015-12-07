package cn.apputest.caas.extinterface.iisiot;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 简历表
 */
public class ResponseHead implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1538124557591112787L;

	//状态码：0成功
    public String resultCode;
    
    //状态说明
    public String resultMsg;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	@Override
	public String toString() {
		return "ResponseHead [resultCode=" + resultCode + ", resultMsg=" + resultMsg + "]";
	}
	
	
    
}
