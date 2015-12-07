/**
 * 
 */
package cn.apputest.caas.extinterface.iisiot.staticinfo;

import org.json.JSONException;

import cn.apputest.caas.extinterface.iisiot.AbstractRequest;
import cn.apputest.caas.util.IISIOTConstants;

/** 通过TagCode获取VIN
 * 
 * @author andy_hou
 * @date 2015年10月15日
 * 
 */
public class VehicleVINRequest extends AbstractRequest {
	
	private String tagCode;
	
	public VehicleVINRequest(String tagCode) {
		super(IISIOTConstants.GET_VEHICLE_VIN_URL);
		this.tagCode = tagCode;
	}

	public void buildRequestBody() throws JSONException {
		postData.put("tagCode", tagCode);
	}

	public Object postHandle() throws JSONException {
		return jsonResult.get("vin");
	}
}
