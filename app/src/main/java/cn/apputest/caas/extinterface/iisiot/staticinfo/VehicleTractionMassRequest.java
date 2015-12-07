/**
 * 
 */
package cn.apputest.caas.extinterface.iisiot.staticinfo;

import org.json.JSONException;

import cn.apputest.caas.extinterface.iisiot.AbstractRequest;
import cn.apputest.caas.util.IISIOTConstants;

/**
 * 根据标签编码或VIN获取车辆的准牵引总质量
 * @author zh
 *
 */
public class VehicleTractionMassRequest extends AbstractRequest {
	
	private String TagcodeorVIN;
	private int QueryType;
	/**
	 * 
	 * @param TagcodeorVIN 根据type填写对应的Tagcode或VIN
	 * @param type   1 tagCode 2VIN
	 */
	public VehicleTractionMassRequest(String TagcodeorVIN,int type) {
		super(IISIOTConstants.GET_VEHICLE_TRACTION_MASS_URL);
		this.QueryType = type;
		this.TagcodeorVIN = TagcodeorVIN;
	}

	public void buildRequestBody() throws JSONException {
		postData.put("type", QueryType);
		postData.put("tagCodeorVIN", TagcodeorVIN);
		
	}
	/**
	 * 获取车辆的整备质量
	 * @return
	 * @throws JSONException 
	 */
	public int getTractionMass() throws JSONException{
		return jsonResult.getInt("tractionMass");
	}
	public Object postHandle() throws JSONException {
		return jsonResult.getInt("tractionMass");
	}
}
