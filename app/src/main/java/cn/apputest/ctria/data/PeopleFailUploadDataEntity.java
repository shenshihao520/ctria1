package cn.apputest.ctria.data;

/**
 * 
 * @author Shenshihao 人员信息 失败重传 实体
 * 
 */
public class PeopleFailUploadDataEntity {
	private Integer ID;
	private String userID;
	private String baseStationID;
	private String status4DriverLicense;
	private String status4JobCert;
	private String DriverName;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getBaseStationID() {
		return baseStationID;
	}

	public void setBaseStationID(String baseStationID) {
		this.baseStationID = baseStationID;
	}

	public String getStatus4DriverLicense() {
		return status4DriverLicense;
	}

	public void setStatus4DriverLicense(String status4DriverLicense) {
		this.status4DriverLicense = status4DriverLicense;
	}

	public String getStatus4JobCert() {
		return status4JobCert;
	}

	public void setStatus4JobCert(String status4JobCert) {
		this.status4JobCert = status4JobCert;
	}

}
