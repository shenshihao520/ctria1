/**
 * 
 */
package cn.apputest.caas.extinterface.iisiot.staticinfo;

import org.json.JSONException;

import cn.apputest.caas.extinterface.iisiot.AbstractRequest;
import cn.apputest.caas.util.IISIOTConstants;

/** 通过VIN获取车牌号
 * 
 * @author andy_hou
 * @date 2015年10月15日
 * 
 */
public class VehiclePlateNumberRequest extends AbstractRequest {
	
	private String tagCodeORVIN;
	private int type;
	public VehiclePlateNumberRequest(String tagCodeORVIN,int type) {
		super(IISIOTConstants.GET_VEHICLE_PLATE_NUMBER_URL);
		this.tagCodeORVIN = tagCodeORVIN;
		this.type = type;
	}
	public void buildRequestBody() throws JSONException {
		postData.put("type", type);
    	postData.put("tagCodeorVIN", tagCodeORVIN);
	}

	public Object postHandle() throws JSONException {
		// TODO Auto-generated method stub
		return jsonResult.get("plateNo");
	}
	
	public String getPlateNo() throws JSONException{
		
		return (String)jsonResult.get("plateNo");
	}

	
	
}
