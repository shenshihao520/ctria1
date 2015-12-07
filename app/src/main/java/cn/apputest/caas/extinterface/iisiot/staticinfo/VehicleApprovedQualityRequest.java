/**
 * 
 */
package cn.apputest.caas.extinterface.iisiot.staticinfo;

import org.json.JSONException;

import cn.apputest.caas.extinterface.iisiot.AbstractRequest;
import cn.apputest.caas.util.IISIOTConstants;

/** 通过VIN获取载重量
 * 
 * @author andy_hou
 * @date 2015年10月15日
 * 
 */
public class VehicleApprovedQualityRequest extends AbstractRequest {
	
	private String tagCodeORVIN;
	private int type;
	public VehicleApprovedQualityRequest(String tagCodeORVIN,int type) {
		super(IISIOTConstants.GET_VEHICLE_APPROVE_QUALITY_URL);
		this.tagCodeORVIN = tagCodeORVIN;
		this.type = type;
	}

	public void buildRequestBody() throws JSONException {
		postData.put("type", type);
    	postData.put("tagCodeorVIN", tagCodeORVIN);
	}

	public Object postHandle() throws JSONException {
		// TODO Auto-generated method stub
		return jsonResult.get("approvedQuality");
	}
}
