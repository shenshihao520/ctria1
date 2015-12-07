package cn.apputest.ctria.data;

/**
 * 
 * @author shenshihao 危化品信息实体
 * 
 */
public class CargoEmergencyMethodDataEntity {
	private String ID;
	private String cargonameID;
	private String cargoname;
	private String cargoemergencymethod;

	public CargoEmergencyMethodDataEntity() {

		// TODO Auto-generated constructor stub
	}

	public CargoEmergencyMethodDataEntity(String cargonameID, String cargoname,
			String cargoemergencymethod) {

		this.cargonameID = cargonameID;
		this.cargoname = cargoname;
		this.cargoemergencymethod = cargoemergencymethod;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getCargonameID() {
		return cargonameID;
	}

	public void setCargonameID(String cargonameID) {
		this.cargonameID = cargonameID;
	}

	public String getCargoname() {
		return cargoname;
	}

	public void setCargoname(String cargoname) {
		this.cargoname = cargoname;
	}

	public String getCargoemergencymethod() {
		return cargoemergencymethod;
	}

	public void setCargoemergencymethod(String cargoemergencymethod) {
		this.cargoemergencymethod = cargoemergencymethod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		result = prime
				* result
				+ ((cargoemergencymethod == null) ? 0 : cargoemergencymethod
						.hashCode());
		result = prime * result
				+ ((cargoname == null) ? 0 : cargoname.hashCode());
		result = prime * result
				+ ((cargonameID == null) ? 0 : cargonameID.hashCode());
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
		CargoEmergencyMethodDataEntity other = (CargoEmergencyMethodDataEntity) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		if (cargoemergencymethod == null) {
			if (other.cargoemergencymethod != null)
				return false;
		} else if (!cargoemergencymethod.equals(other.cargoemergencymethod))
			return false;
		if (cargoname == null) {
			if (other.cargoname != null)
				return false;
		} else if (!cargoname.equals(other.cargoname))
			return false;
		if (cargonameID == null) {
			if (other.cargonameID != null)
				return false;
		} else if (!cargonameID.equals(other.cargonameID))
			return false;
		return true;
	}

}
