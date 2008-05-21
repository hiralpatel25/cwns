package gov.epa.owm.mtb.cwns.county;

public class FacilityCountyHelper {
	
	private String countyName;
	private String code;
	private char primary;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public char getPrimary() {
		return primary;
	}
	public void setPrimary(char primary) {
		this.primary = primary;
	}
	public FacilityCountyHelper(String countyName, String code, char primary) {
		super();
		this.countyName = countyName;
		this.code = code;
		this.primary = primary;
	}

}
