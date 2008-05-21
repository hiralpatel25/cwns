package gov.epa.owm.mtb.cwns.congressionalDistrict;

import java.util.Collection;

import org.apache.struts.validator.ValidatorActionForm;

public class CongressionalDistrictForm extends ValidatorActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String conDistrictName = "";
	private char primary = 'N';
	private String mode = "list";
	private String locationId="";
	private String isUpdatable = "N";
	private String showWarningMessage = "N";
	private Long facilityId;
	private long geographicAreaId;
	private String conDistrictId="";
	private Collection conDistrictHelpers;
	
	public String getConDistrictId() {
		return conDistrictId;
	}
	public void setConDistrictId(String conDistrictId) {
		this.conDistrictId = conDistrictId;
	}
	public Long getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
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
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public char getPrimary() {
		return primary;
	}
	public void setPrimary(char primary) {
		this.primary = primary;
	}
	public String getShowWarningMessage() {
		return showWarningMessage;
	}
	public void setShowWarningMessage(String showWarningMessage) {
		this.showWarningMessage = showWarningMessage;
	}
	public Collection getConDistrictHelpers() {
		return conDistrictHelpers;
	}
	public void setConDistrictHelpers(Collection conDistrictHelpers) {
		this.conDistrictHelpers = conDistrictHelpers;
	}
	public String getConDistrictName() {
		return conDistrictName;
	}
	public void setConDistrictName(String conDistrictName) {
		this.conDistrictName = conDistrictName;
	}

}
