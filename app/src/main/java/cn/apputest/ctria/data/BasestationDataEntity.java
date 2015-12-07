package cn.apputest.ctria.data;

import com.baidu.mapapi.model.LatLng;

/** 
 * @author 作者Shihao Shen: 
 * @version 创建时间：2015-11-12 下午6:08:44 
 * 类说明 
 */
public class BasestationDataEntity {
	private Integer ID;
	private LatLng latlng;
	private String name;
	private boolean isbasestation;
	

	public boolean isIsbasestation() {
		return isbasestation;
	}
	public void setIsbasestation(boolean isbasestation) {
		this.isbasestation = isbasestation;
	}
	public LatLng getLatlng() {
		return latlng;
	}
	public void setLatlng(LatLng latlng) {
		this.latlng = latlng;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
