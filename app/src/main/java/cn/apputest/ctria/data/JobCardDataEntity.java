package cn.apputest.ctria.data;

/**
 * 
 * @author shenshihao 从业资格证信息实体
 * 
 */
public class JobCardDataEntity {
	private String ID;
	private String certNumber;
	private String jobInfo;
	private String endDate;

	public String getCertNumber() {
		return certNumber;
	}

	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}

	public String getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(String jobInfo) {
		this.jobInfo = jobInfo;
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
				+ ((certNumber == null) ? 0 : certNumber.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((jobInfo == null) ? 0 : jobInfo.hashCode());
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
		JobCardDataEntity other = (JobCardDataEntity) obj;
		if (certNumber == null) {
			if (other.certNumber != null)
				return false;
		} else if (!certNumber.equals(other.certNumber))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (jobInfo == null) {
			if (other.jobInfo != null)
				return false;
		} else if (!jobInfo.equals(other.jobInfo))
			return false;
		return true;
	}

}
