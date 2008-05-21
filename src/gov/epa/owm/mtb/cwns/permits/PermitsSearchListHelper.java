package gov.epa.owm.mtb.cwns.permits;

public class PermitsSearchListHelper {
	
	private String permitNumber;
	private String permitType;
	private String facilityName;
	private String countyName;
	private String assWithAnotherFac="N";
	public String getAssWithAnotherFac() {
		return assWithAnotherFac;
	}
	public void setAssWithAnotherFac(String assWithAnotherFac) {
		this.assWithAnotherFac = assWithAnotherFac;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public String getPermitNumber() {
		return permitNumber;
	}
	public void setPermitNumber(String permitNumber) {
		this.permitNumber = permitNumber;
	}
	public String getPermitType() {
		return permitType;
	}
	public void setPermitType(String permitType) {
		this.permitType = permitType;
	}
	
}
