package cn.apputest.ctria.data;

/**
 * 
 * @author Shihao Shen 道路运输证实体
 */
public class TransportCardDataEntity {
	private String ID;
	private String plateNumber;
	private String transportInfo;
	private String endDate;

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getTransportInfo() {
		return transportInfo;
	}

	public void setTransportInfo(String transportInfo) {
		this.transportInfo = transportInfo;
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
		result = prime * result
				+ ((transportInfo == null) ? 0 : transportInfo.hashCode());
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
		TransportCardDataEntity other = (TransportCardDataEntity) obj;
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
		if (transportInfo == null) {
			if (other.transportInfo != null)
				return false;
		} else if (!transportInfo.equals(other.transportInfo))
			return false;
		return true;
	}

}
