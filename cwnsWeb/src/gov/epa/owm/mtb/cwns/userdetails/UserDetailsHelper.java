/**
 * 
 */
package gov.epa.owm.mtb.cwns.userdetails;

import java.util.Collection;

/**
 * @author mconnors
 *
 */
public class UserDetailsHelper{
	
	private String locationId;
	private String role;
	private Collection accessLevels;
	private String limitedFacilities;


	public String getLimitedFacilities() {
		return limitedFacilities;
	}
	public void setLimitedFacilities(String limitedFacilities) {
		this.limitedFacilities = limitedFacilities;
	}
	public UserDetailsHelper(String locationId, String role, Collection accessLevels, String limitedFacilities) {
		super();
		this.locationId = locationId;
		this.role = role;
		this.accessLevels = accessLevels;
		this.limitedFacilities = limitedFacilities;
	}
	public Collection getAccessLevels() {
		return accessLevels;
	}
	public void setAccessLevels(Collection accessLevels) {
		this.accessLevels = accessLevels;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}


}
