/**
 * 
 */
package gov.epa.owm.mtb.cwns.usersearch;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * @author jlaviset
 *
 */
public class UserSearchForm extends ActionForm {
	
	private String action ="";
	private String locationType="";	
	private String status="A";
	private String[] accessLevelIds;
	private String locationId="";	// selected Location (NY, NJ, Regional, etc ...)
	private String adminUserLocationType;
	private String keyword; 
	
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String[] getAccessLevelIds() {
		return accessLevelIds;
	}
	public void setAccessLevelIds(String[] accessLevelIds) {
		this.accessLevelIds = accessLevelIds;
	}
	public String getAdminUserLocationType() {
		return adminUserLocationType;
	}
	public void setAdminUserLocationType(String adminUserLocationType) {
		this.adminUserLocationType = adminUserLocationType;
	}

}
