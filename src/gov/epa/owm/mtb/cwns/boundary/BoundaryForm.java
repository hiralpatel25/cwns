package gov.epa.owm.mtb.cwns.boundary;

import java.util.Collection;

import org.apache.struts.validator.ValidatorActionForm;

public class BoundaryForm extends ValidatorActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String boundaryName = "";
	private String boundaryType = "";
	private char primary = 'N';
	private String mode = "list";
	private String locationId="";
	private String isUpdatable = "N";
	private String showWarningMessage = "N";
	private Long facilityId;
	private long geographicAreaId;
	private long boundaryId;
	private Collection geographicAreaBoundaries;
	
	public long getBoundaryId() {
		return boundaryId;
	}
	public void setBoundaryId(long boundaryId) {
		this.boundaryId = boundaryId;
	}
	public String getBoundaryName() {
		return boundaryName;
	}
	public void setBoundaryName(String boundaryName) {
		this.boundaryName = boundaryName;
	}
	public Long getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}
	public Collection getGeographicAreaBoundaries() {
		return geographicAreaBoundaries;
	}
	public void setGeographicAreaBoundaries(Collection geographicAreaBoundaries) {
		this.geographicAreaBoundaries = geographicAreaBoundaries;
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

	public String getBoundaryType() {
		return boundaryType;
	}

	public void setBoundaryType(String boundaryType) {
		this.boundaryType = boundaryType;
	}
}
