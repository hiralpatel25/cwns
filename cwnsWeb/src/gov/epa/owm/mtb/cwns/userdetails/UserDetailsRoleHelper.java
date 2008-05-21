/**
 * 
 */
package gov.epa.owm.mtb.cwns.userdetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author mconnors
 *
 */
public class UserDetailsRoleHelper implements Serializable{
	
	private String     locationType;
	private String     location;
	private Collection facilities;
	
	public Collection getFacilities() {
		return facilities;
	}
	public void setFacilities(Collection facilities) {
		this.facilities = facilities;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
}
