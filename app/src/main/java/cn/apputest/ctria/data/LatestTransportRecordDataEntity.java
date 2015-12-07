package cn.apputest.ctria.data;

/**
 * 
 * @author shenshihao 最新货运记录实体
 * 
 */
public class LatestTransportRecordDataEntity {
	private Integer ID;

	private String plateNumber;
	private String plateNumber_gua;
	private String cargoname;
	private String transportrecordInfo;
	private Integer weight;

	public String getPlateNumber_gua() {
		return plateNumber_gua;
	}

	public void setPlateNumber_gua(String plateNumber_gua) {
		this.plateNumber_gua = plateNumber_gua;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getCargoname() {
		return cargoname;
	}

	public void setCargoname(String cargoname) {
		this.cargoname = cargoname;
	}

	public String getTransportrecordInfo() {
		return transportrecordInfo;
	}

	public void setTransportrecordInfo(String transportrecordInfo) {
		this.transportrecordInfo = transportrecordInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cargoname == null) ? 0 : cargoname.hashCode());
		result = prime * result
				+ ((plateNumber == null) ? 0 : plateNumber.hashCode());
		result = prime * result
				+ ((plateNumber_gua == null) ? 0 : plateNumber_gua.hashCode());
		result = prime
				* result
				+ ((transportrecordInfo == null) ? 0 : transportrecordInfo
						.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LatestTransportRecordDataEntity other = (LatestTransportRecordDataEntity) obj;
		if (cargoname == null) {
			if (other.cargoname != null)
				return false;
		} else if (!cargoname.equals(other.cargoname))
			return false;
		if (plateNumber == null) {
			if (other.plateNumber != null)
				return false;
		} else if (!plateNumber.equals(other.plateNumber))
			return false;
		if (plateNumber_gua == null) {
			if (other.plateNumber_gua != null)
				return false;
		} else if (!plateNumber_gua.equals(other.plateNumber_gua))
			return false;
		if (transportrecordInfo == null) {
			if (other.transportrecordInfo != null)
				return false;
		} else if (!transportrecordInfo.equals(other.transportrecordInfo))
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		return true;
	}

}
