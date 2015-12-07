package cn.apputest.ctria.data;

/**
 * 
 * @author shenshihao 驾驶证信息实体
 * 
 */
public class DriverLicenseCardDataEntity {
	private String ID;
	private String drivername;
	private String driverLicenseNumber;
	private String driverInfo;
	private String endDate;

	public String getDrivername() {
		return drivername;
	}

	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}

	public String getDriverLicenseNumber() {
		return driverLicenseNumber;
	}

	public void setDriverLicenseNumber(String driverLicenseNumber) {
		this.driverLicenseNumber = driverLicenseNumber;
	}

	public String getDriverInfo() {
		return driverInfo;
	}

	public void setDriverInfo(String driverInfo) {
		this.driverInfo = driverInfo;
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
		result = prime * result
				+ ((driverInfo == null) ? 0 : driverInfo.hashCode());
		result = prime
				* result
				+ ((driverLicenseNumber == null) ? 0 : driverLicenseNumber
						.hashCode());
		result = prime * result
				+ ((drivername == null) ? 0 : drivername.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
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
		DriverLicenseCardDataEntity other = (DriverLicenseCardDataEntity) obj;
		if (driverInfo == null) {
			if (other.driverInfo != null)
				return false;
		} else if (!driverInfo.equals(other.driverInfo))
			return false;
		if (driverLicenseNumber == null) {
			if (other.driverLicenseNumber != null)
				return false;
		} else if (!driverLicenseNumber.equals(other.driverLicenseNumber))
			return false;
		if (drivername == null) {
			if (other.drivername != null)
				return false;
		} else if (!drivername.equals(other.drivername))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		return true;
	}

}
