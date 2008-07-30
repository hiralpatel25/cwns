package gov.epa.owm.mtb.cwns.common;

import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import oracle.portal.provider.v2.render.PortletRenderRequest;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsHelper;

/**
*
* @author Matt Connors
* @version 1.0
* 
* The User class holds information regarding the current user. 
* This includes security related information used for 
* authorization purposes. The User object gets attached to the HTTP 
* Session object via a "setAttribute(...)" statement.  
* 
*/
public class CurrentUser  implements java.io.Serializable{

	public static final String CWNS_USER = "cwnsUser";
	
	/* SECURITY ACCESS PRIVILEGES  */
	
	/* Federal */
	public static final Long FEDERAL_USER_MANAGEMENT 	= new Long(1);
	public static final Long ADMIN_FEDERAL_REFERENCE 	= new Long(2);
	public static final Long FEDERAL_REVIEW             = new Long(3);
	
	/* State */
	public static final Long STATE_USER_MANAGEMENT		= new Long(4);
	public static final Long ADMIN_STATE_REFERENCE  	= new Long(5);
	public static final Long FACILITY_UPDATE 			= new Long(6);

	public static final Long SUBMIT_FOR_FEDERAL_REVIEW 	= new Long(7);
	
	/* Local */
	public static final Long LOCAL_USER_MANAGEMENT		= new Long(8);
	public static final Long FACILITY_UPDATE_PARTIAL 	= new Long(9);
	public static final Long FACILITY_FEEDBACK 			= new Long(10);
	public static final Long SUBMIT_FOR_STATE_REVIEW 	= new Long(11);
	
    /* view */
	public static final Long VIEW 	= new Long(12);
	
	protected String userId;
	protected String oidUserId;

	protected String firstName;
	protected String lastName;
	protected String fullName;

	protected String phoneNumber;

	protected String comments;

	protected String status;

	protected UserRole  currentRole;

	Collection roles;

	public CurrentUser() {
		super();
		roles = new ArrayList();
	}
	
	/**
	 * Used to determine if a user has the specified access privilege
	 * based on their current UserRole.
	 * @param accessPrivilege The access privilege being checked
	 * @return true if the user has this particular access privilege, 
	 * false otherwise.
	 */
	public boolean isAuth(Long accessPrivilege) {
		boolean auth = false;
		if (accessPrivilege != null) {
			auth = (getCurrentRole().getPrivileges().get(accessPrivilege) != null);
		}
		return auth;
	}
	
	/**
	 * Used to determine if a user has administrative privileges.
	 * @return true if this is an admin user, 
	 * false otherwise.
	 */
	public boolean isAdmin() {
		HashMap privileges = getCurrentRole().getPrivileges();
		
		// If an admin privilege is found return true
		if (privileges.containsKey(ADMIN_FEDERAL_REFERENCE)  ||
			privileges.containsKey(ADMIN_STATE_REFERENCE))  {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Used to determine if a user has User Manager privileges.
	 * @return true if this is a User Manager, 
	 * false otherwise.
	 */
	public boolean isUserManager() {
		HashMap privileges = getCurrentRole().getPrivileges();
		
		// If a User Mananger privilege is found return true
		if (privileges.containsKey(FEDERAL_USER_MANAGEMENT) || 
			privileges.containsKey(STATE_USER_MANAGEMENT)  ||
			privileges.containsKey(LOCAL_USER_MANAGEMENT))  {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Using the roleInfo argumentn determine if this user has the access privleges 
	 * to modify the user role.
	 * @param roleInfo The format of this String is <LocationTypeId:LocationId>
	 * @return
	 */
	public boolean isRoleEditable(String roleInfo) {

		String[] ri = roleInfo.split(":");
		String locationTypeId = ri[0];
		String locationId     = ri[1];
		
		String userLocationTypeId = currentRole.getLocationTypeId();
		String userLocationId   = currentRole.getLocationId();
		
		// Current user - Federal
		if ("Federal".equals(userLocationTypeId)) {
			if (!"Local".equals(locationTypeId)) {
				return true;
			}
		}
		
		// Current user - State
		if ("State".equals(userLocationTypeId)) {
			if ("State".equals(locationTypeId) ||
				"Local".equals(locationTypeId)) {
				
				if (userLocationId.equals(locationId)){
					return true;
				}
			}
		}
		
		// Current user - Local
		if ("Local".equals(userLocationTypeId)) {
			if ("Local".equals(locationTypeId)&&
				userLocationId.equals(locationId)){
					return true;
			}
		}
		
		return false;
	}
	
	public boolean isEditableByLocal(String isLimitedFacilities){
		String userLocationTypeId = currentRole.getLocationTypeId();
		boolean isLimited = currentRole.isLimited();
		if ("Local".equals(userLocationTypeId) && isLimited) {
			if ("N".equals(isLimitedFacilities)){
					return false;
			}
		}
		return true;
	} 
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserRole getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(UserRole currentRole) {
		this.currentRole = currentRole;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Collection getRoles() {
		return roles;
	}

	public void setRoles(Collection roles) {
		this.roles = roles;
	}

	/**
	 * Returns the cwnsUserSettingID. The cwnsUserSettingId is the key into the MY_FACILITIES table
	 * @return
	 */
	public String getUserAndRole() {
		return getUserId() + ":" +getCurrentRole().getLocationTypeId();
	}

	public String getOidUserId() {
		return oidUserId;
	}

	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}
	
	public boolean isNonLocalUser(){
		return this.getCurrentRole().getLocationTypeId().equals(UserService.LOCATION_TYPE_ID_LOCAL)?false:true;
	}
}