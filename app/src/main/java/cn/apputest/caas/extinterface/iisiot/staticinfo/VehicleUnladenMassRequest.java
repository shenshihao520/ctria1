/**
 * 
 */
package cn.apputest.caas.extinterface.iisiot.staticinfo;

import org.json.JSONException;

import cn.apputest.caas.extinterface.iisiot.AbstractRequest;
import cn.apputest.caas.util.IISIOTConstants;

/**
 * 根据标签编码或VIN获取车辆的整备质量
 * @author zh
 *
 */
public class VehicleUnladenMassRequest extends AbstractRequest {
	
	private String TagcodeorVIN;
	private int QueryType;
	/**
	 * 
	 * @param TagcodeorVIN 根据type填写对应的Tagcode或VIN
	 * @param type   1 tagCode 2VIN
	 */
	public VehicleUnladenMassRequest(String tagcodeorVIN,int type) {
		super(IISIOTConstants.GET_VEHICLE_UNLADEN_MASS_URL);
		this.TagcodeorVIN = tagcodeorVIN;
		this.QueryType = type;
	}

	public void buildRequestBody() throws JSONException {
		postData.put("type",QueryType);
		postData.put("tagCodeorVIN", TagcodeorVIN);
	}
	/**
	 * 获取车辆的整备质量
	 * @return
	 * @throws JSONException 
	 */
	public int getUnladenMass() throws JSONException{
		return jsonResult.getInt("unladenMass");
	}
	public Object postHandle() throws JSONException {
		return jsonResult.getInt("unladenMass");
	}
}
