package gov.epa.owm.mtb.cwns.county;

import java.util.Collection;

import org.apache.struts.validator.ValidatorActionForm;

public class FacilityCountyForm extends ValidatorActionForm {
		
	private static final long serialVersionUID = 1L;
	private String fipsCode = "";
	private String countyName = "";
	private char primary = 'N';
	private String mode = "list";
	private String locationId="";
	private String isUpdatable = "N";
	private String showWarningMessage = "N";
	private Long facilityId;
	private long geographicAreaId;
	private long countyId;
	
	private Collection geographicAreaCounties;
		
	public char getPrimary() {
		return primary;
	}
	public void setPrimary(char primary) {
		this.primary = primary;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public Long getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getFipsCode() {
		return fipsCode;
	}
	public void setFipsCode(String fipsCode) {
		this.fipsCode = fipsCode;
	}
	public Collection getGeographicAreaCounties() {
		return geographicAreaCounties;
	}
	public void setGeographicAreaCounties(Collection geographicAreaCounties) {
		this.geographicAreaCounties = geographicAreaCounties;
	}
	public long getCountyId() {
		return countyId;
	}
	public void setCountyId(long countyId) {
		this.countyId = countyId;
	}
	public long getGeographicAreaId() {
		return geographicAreaId;
	}
	public void setGeographicAreaId(long geographicAreaId) {
		this.geographicAreaId = geographicAreaId;
	}
	public String getIsUpdatable() {
		return isUpdatable;
	}
	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}
	public String getShowWarningMessage() {
		return showWarningMessage;
	}
	public void setShowWarningMessage(String showWarningMessage) {
		this.showWarningMessage = showWarningMessage;
	}

}
