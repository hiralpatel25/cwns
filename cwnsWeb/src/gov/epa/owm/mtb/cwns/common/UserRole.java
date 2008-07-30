package gov.epa.owm.mtb.cwns.common;
/**
*
* @author Lockheed Martin CWNS Team
* @version 1.0
* 
* The UserRole class holds authorization information for a user. 
* A User object can have 1 to many UserRole objects associated with it.
* At any given time there is only one current UserRole object assocatiated
* with a user. 
* 
*/
import java.util.HashMap;

public class UserRole  {

	protected String    locationTypeId; // Local, State, Regional, Federal

	protected String    locationId;

	protected boolean   currentRole;
	
	protected boolean limited;

	protected HashMap 	privileges;

	protected HashMap facilities;

	public UserRole(String locationTypeId, 
					String locationId,
					boolean currentRole, 
					HashMap privileges, 
					HashMap facilities, boolean limited) {

		this();
		this.locationTypeId = locationTypeId;
		this.locationId     = locationId;
		this.currentRole    = currentRole;
		this.privileges     = privileges;
		this.facilities     = facilities;
		this.limited 		= limited;
}

	
	public UserRole() {
		currentRole = false;
		privileges  = new HashMap();
		facilities  = new HashMap();
	}

//	public UserRole(String type, String location, boolean currentRole ) {
//		this();
//		this.locationTypeId     = type;
//		this.locationId = location;
//	}



	public void setLocationTypeId(String type) {
		this.locationTypeId = type;
	}


	public String getLocationTypeId() {
		return locationTypeId;
	}


	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}


	public String getLocationId() {
		return locationId;
	}

	public boolean isCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(boolean currentRole) {
		this.currentRole = currentRole;
	}


	public HashMap getPrivileges() {
		return privileges;
	}

	public void setPrivileges(HashMap privileges) {
		this.privileges = privileges;
	}


	public HashMap getFacilities() {
		return facilities;
	}


	public void setFacilities(HashMap facilities) {
		this.facilities = facilities;
	}


	public boolean isLimited() {
		return limited;
	}


	public void setLimited(boolean limited) {
		this.limited = limited;
	}


}

