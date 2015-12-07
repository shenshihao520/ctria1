package cn.apputest.ctria.data;
/** 
 * @author 作者Shihao Shen: 
 * @version 创建时间：2015-11-24 下午5:47:30 
 * 类说明 
 */
public class BasestationPassCarRecordDataEntity {
	private Integer ID;
	private String carnum;
	private String passingtime;
	public String getCarnum() {
		return carnum;
	}
	public void setCarnum(String carnum) {
		this.carnum = carnum;
	}
	public String getPassingtime() {
		return passingtime;
	}
	public void setPassingtime(String passingtime) {
		this.passingtime = passingtime;
	}
	
}
