package cn.apputest.ctria.data;

/**
 * 
 * @author Shenshihao 准行证 信息实体
 * 
 */
public class PermitCardDataEntity {
	private String ID;
	private String plateNumber;
	private String permitInfo;
	private String endDate;

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getPermitInfo() {
		return permitInfo;
	}

	public void setPermitInfo(String permitInfo) {
		this.permitInfo = permitInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((permitInfo == null) ? 0 : permitInfo.hashCode());
		result = prime * result
				+ ((plateNumber == null) ? 0 : plateNumber.hashCode());
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
		PermitCardDataEntity other = (PermitCardDataEntity) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (permitInfo == null) {
			if (other.permitInfo != null)
				return false;
		} else if (!permitInfo.equals(other.permitInfo))
			return false;
		if (plateNumber == null) {
			if (other.plateNumber != null)
				return false;
		} else if (!plateNumber.equals(other.plateNumber))
			return false;
		return true;
	}

}
