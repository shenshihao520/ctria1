package cn.apputest.ctria.data;

/**
 * 
 * @author Shenshihao 车辆信息失败重传 实体
 * 
 */
public class CarsFailUploadDataEntity {
	private Integer ID;
	private String userID;
	private String baseStationID;
	private String isOverWeight;
	private String Status4PermitRunCert;
	private String status4TransportCert_Head;
	private String status4TransportCert_Tail;
	private String status4InsuranceCert_Head;
	private String status4InsuranceCert_Cargo;
	private String status4InsuranceCert_Tail;
	private String status4SpecialEquipUsage;
	private String plateNumber;

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
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

	public String getIsOverWeight() {
		return isOverWeight;
	}

	public void setIsOverWeight(String isOverWeight) {
		this.isOverWeight = isOverWeight;
	}

	public String getStatus4PermitRunCert() {
		return Status4PermitRunCert;
	}

	public void setStatus4PermitRunCert(String status4PermitRunCert) {
		Status4PermitRunCert = status4PermitRunCert;
	}

	public String getStatus4TransportCert_Head() {
		return status4TransportCert_Head;
	}

	public void setStatus4TransportCert_Head(String status4TransportCert_Head) {
		this.status4TransportCert_Head = status4TransportCert_Head;
	}

	public String getStatus4TransportCert_Tail() {
		return status4TransportCert_Tail;
	}

	public void setStatus4TransportCert_Tail(String status4TransportCert_Tail) {
		this.status4TransportCert_Tail = status4TransportCert_Tail;
	}

	public String getStatus4InsuranceCert_Head() {
		return status4InsuranceCert_Head;
	}

	public void setStatus4InsuranceCert_Head(String status4InsuranceCert_Head) {
		this.status4InsuranceCert_Head = status4InsuranceCert_Head;
	}

	public String getStatus4InsuranceCert_Cargo() {
		return status4InsuranceCert_Cargo;
	}

	public void setStatus4InsuranceCert_Cargo(String status4InsuranceCert_Cargo) {
		this.status4InsuranceCert_Cargo = status4InsuranceCert_Cargo;
	}

	public String getStatus4InsuranceCert_Tail() {
		return status4InsuranceCert_Tail;
	}

	public void setStatus4InsuranceCert_Tail(String status4InsuranceCert_Tail) {
		this.status4InsuranceCert_Tail = status4InsuranceCert_Tail;
	}

	public String getStatus4SpecialEquipUsage() {
		return status4SpecialEquipUsage;
	}

	public void setStatus4SpecialEquipUsage(String status4SpecialEquipUsage) {
		this.status4SpecialEquipUsage = status4SpecialEquipUsage;
	}

}
