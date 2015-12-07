package cn.apputest.ctria.data;

/**
 * 
 * @author Shihao Shen 特种设备使用方法（运输证）实体
 */
public class SpecialEquipUsageDataEntity {
	private String ID;
	private String plateNumber;
	private String specialequipmentInfo;
	private String endDate;

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getSpecialequipmentInfo() {
		return specialequipmentInfo;
	}

	public void setSpecialequipmentInfo(String specialequipmentInfo) {
		this.specialequipmentInfo = specialequipmentInfo;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((plateNumber == null) ? 0 : plateNumber.hashCode());
		result = prime
				* result
				+ ((specialequipmentInfo == null) ? 0 : specialequipmentInfo
						.hashCode());
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
		SpecialEquipUsageDataEntity other = (SpecialEquipUsageDataEntity) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (plateNumber == null) {
			if (other.plateNumber != null)
				return false;
		} else if (!plateNumber.equals(other.plateNumber))
			return false;
		if (specialequipmentInfo == null) {
			if (other.specialequipmentInfo != null)
				return false;
		} else if (!specialequipmentInfo.equals(other.specialequipmentInfo))
			return false;
		return true;
	}

}
