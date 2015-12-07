package cn.apputest.ctria.data;

/**
 * 
 * @author Shihao Shen 版本信息实体
 * 
 */
public class VersionInfo {
	private String ID;
	private String versionCode;
	private String verionNumber;
	private String releaseNote;
	private String releaseDate;

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVerionNumber() {
		return verionNumber;
	}

	public void setVerionNumber(String verionNumber) {
		this.verionNumber = verionNumber;
	}

	public String getReleaseNote() {
		return releaseNote;
	}

	public void setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

}
