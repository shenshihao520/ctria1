package cn.apputest.ctria.data;

/**
 * 
 * @author shenshihao 车辆基本信息实体
 * 
 */
public class CarBasicInfoDataEntity {
	private String ID;
	private String plateNumber;
	private double maxweight;
	private Integer status4PermitRunCert;
	private Integer status4TransportCertHead;
	private Integer status4TransportCertTail;
	private Integer status4InsuranceCertHead;
	private Integer status4InsuranceCertCargo;
	private Integer status4InsuranceCertTail;
	private Integer status4SEquipCheck;
	private double	traction_quality;   //牵引载重量
	private double  check_quality;		//核定
	private double  reorganize_quality; 		//	整备
	
	private double  check_quality_tail;		//车尾核定
	private double maxweight_tail;           //车尾max
	public double getCheck_quality_tail() {
		return check_quality_tail;
	}

	public void setCheck_quality_tail(double check_quality_tail) {
		this.check_quality_tail = check_quality_tail;
	}

	public double getMaxweight_tail() {
		return maxweight_tail;
	}

	public void setMaxweight_tail(double maxweight_tail) {
		this.maxweight_tail = maxweight_tail;
	}

	public double getTraction_quality() {
		return traction_quality;
	}

	public void setTraction_quality(double traction_quality) {
		this.traction_quality = traction_quality;
	}

	public double getCheck_quality() {
		return check_quality;
	}

	public void setCheck_quality(double check_quality) {
		this.check_quality = check_quality;
	}

	public double getReorganize_quality() {
		return reorganize_quality;
	}

	public void setReorganize_quality(double reorganize_quality) {
		this.reorganize_quality = reorganize_quality;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public double getMaxweight() {
		return maxweight;
	}

	public void setMaxweight(double maxweight) {
		this.maxweight = maxweight;
	}

	public Integer getStatus4PermitRunCert() {
		return status4PermitRunCert;
	}

	public void setStatus4PermitRunCert(Integer status4PermitRunCert) {
		this.status4PermitRunCert = status4PermitRunCert;
	}

	public Integer getStatus4TransportCertHead() {
		return status4TransportCertHead;
	}

	public void setStatus4TransportCertHead(Integer status4TransportCertHead) {
		this.status4TransportCertHead = status4TransportCertHead;
	}

	public Integer getStatus4TransportCertTail() {
		return status4TransportCertTail;
	}

	public void setStatus4TransportCertTail(Integer status4TransportCertTail) {
		this.status4TransportCertTail = status4TransportCertTail;
	}

	public Integer getStatus4InsuranceCertHead() {
		return status4InsuranceCertHead;
	}

	public void setStatus4InsuranceCertHead(Integer status4InsuranceCertHead) {
		this.status4InsuranceCertHead = status4InsuranceCertHead;
	}

	public Integer getStatus4InsuranceCertCargo() {
		return status4InsuranceCertCargo;
	}

	public void setStatus4InsuranceCertCargo(Integer status4InsuranceCertCargo) {
		this.status4InsuranceCertCargo = status4InsuranceCertCargo;
	}

	public Integer getStatus4InsuranceCertTail() {
		return status4InsuranceCertTail;
	}

	public void setStatus4InsuranceCertTail(Integer status4InsuranceCertTail) {
		this.status4InsuranceCertTail = status4InsuranceCertTail;
	}

	public Integer getStatus4SEquipCheck() {
		return status4SEquipCheck;
	}

	public void setStatus4SEquipCheck(Integer status4sEquipCheck) {
		status4SEquipCheck = status4sEquipCheck;
	}

}
