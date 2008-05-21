package gov.epa.owm.mtb.cwns.watershed;

import java.util.Collection;

import org.apache.struts.validator.ValidatorActionForm;

public class WatershedForm extends ValidatorActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String watershedName = "";
	private char primary = 'N';
	private String mode = "list";
	private String locationId="";
	private String isUpdatable = "N";
	private String showWarningMessage = "N";
	private Long facilityId;
	private long geographicAreaId;
	private String watershedId="";
	private Collection geographicAreaWatersheds;
	
	
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
	public Collection getGeographicAreaWatersheds() {
		return geographicAreaWatersheds;
	}
	public void setGeographicAreaWatersheds(Collection geographicAreaWatersheds) {
		this.geographicAreaWatersheds = geographicAreaWatersheds;
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
	public String getWatershedId() {
		return watershedId;
	}
	public void setWatershedId(String watershedId) {
		this.watershedId = watershedId;
	}
	public String getWatershedName() {
		return watershedName;
	}
	public void setWatershedName(String watershedName) {
		this.watershedName = watershedName;
	}

}
