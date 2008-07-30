/**
 * 
 */
package gov.epa.owm.mtb.cwns.userdetails;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * @author matt connors
 *
 */
public class UserDetailsForm extends ActionForm {
	
	private String act ="";
	private String name ="";
	private String cwnsUserId ="";
	private String oidUserId = "";
	private String phone ="";
	private String email="";
	private String comments="";
	private String displayData="";
	private String status="";
	
	private String regType="";   // registration type - self or admin
	private String regNotificationComments= "";
	
	private String primaryRoleInfo="";
	private String selectedRoleInfoId= "";		// Holds user selection

	private String currentLocId="";	  			// Used in adding & editing a role
	private String currentLocationTypeId=""; 	// Used in adding & editing a role 
	
	private String roleDisplay=""; 			// Controls whether Role information is displayed or 
											//   hidden on the jsp.
	private String roleMode = ""; 			// "add" or "edit"
	
	private boolean enableAddRoleLink = true;
	
	// Indicates if role information must be saved before facilities can be added
	private String SaveRoleBeforeFacilities= "";    
	                                              
	
	private String allFacilities="Y";  // 'Y' or 'N'
	private ArrayList facilityIds = new ArrayList();  // Identifies Facilities to be selected
	private String strFacilityIds="";
	
	
	private String[] accessLevelIds;
	
	private Collection helpers;
	private UserDetailsRoleHelper udrHelper;
	
	private String affiliation="";
	private String title="";
	private String facilityList="";
	private String townAndCountyList="";


	
	public String getSelectedRoleLocId() {
		String [] roleIds =  getSelectedRoleInfoId().split(":");
		return roleIds[1]; 
		
	}
	

	
	public String getSelectedRoleLocTypeId() {
		String [] roleIds =  getSelectedRoleInfoId().split(":");
		return roleIds[0]; 
		
	}

	
	public void resetRoleData(boolean display) {
		String [] ali = {""};
		setAccessLevelIds(ali);
		setCurrentLocationTypeId("");
		setCurrentLocId("");
//		setAct(UserDetailsAction.ACTION_NEW_ROLE);
		setRoleMode(UserDetailsAction.ROLE_ADD);
		
		if (display) {
			setRoleDisplay(UserDetailsAction.DISPLAY_ROLE_INFO);
		} else {
			setRoleDisplay(UserDetailsAction.HIDE_ROLE_INFO);
		}
	}
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getDisplayData() {
		return displayData;
	}

	public void setDisplayData(String displayData) {
		this.displayData = displayData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Collection getHelpers() {
		return helpers;
	}

	public void setHelpers(Collection helpers) {
		this.helpers = helpers;
	}

	public String getPrimaryRoleInfo() {
		return primaryRoleInfo;
	}

	public void setPrimaryRoleInfo(String primaryRoleLocationId) {
		this.primaryRoleInfo = primaryRoleLocationId;
	}

	public String getCurrentLocationTypeId() {
		return currentLocationTypeId;
	}

	public void setCurrentLocationTypeId(String currentLocationTypeId) {
		this.currentLocationTypeId = currentLocationTypeId;
	}

	public String getAllFacilities() {
		return allFacilities;
	}

	public void setAllFacilities(String allFacilities) {
		this.allFacilities = allFacilities;
	}

	public String getRoleDisplay() {
		return roleDisplay;
	}

	public void setRoleDisplay(String roleDisplay) {
		this.roleDisplay = roleDisplay;
	}

	public String getCwnsUserId() {
		return cwnsUserId;
	}

	public void setCwnsUserId(String cwnsUserId) {
		this.cwnsUserId = cwnsUserId;
	}

	public UserDetailsRoleHelper getUdrHelper() {
		return udrHelper;
	}

	public void setUdrHelper(UserDetailsRoleHelper udrHelper) {
		this.udrHelper = udrHelper;
	}

	public String[] getAccessLevelIds() {
		return accessLevelIds;
	}

	public void setAccessLevelIds(String[] accessLevelIds) {
		this.accessLevelIds = accessLevelIds;
	}

	public String getCurrentLocId() {
		return currentLocId;
	}

	public void setCurrentLocId(String selectedLocation) {
		this.currentLocId = selectedLocation;
	}

	public String getOidUserId() {
		return oidUserId;
	}

	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}

	public String getSaveRoleBeforeFacilities() {
		return SaveRoleBeforeFacilities;
	}

	public void setSaveRoleBeforeFacilities(String saveRoleBeforeFacilities) {
		SaveRoleBeforeFacilities = saveRoleBeforeFacilities;
	}

	public String getRoleMode() {
		return roleMode;
	}

	public void setRoleMode(String roleMode) {
		this.roleMode = roleMode;
	}

	public String getSelectedRoleInfoId() {
		return selectedRoleInfoId;
	}

	public void setSelectedRoleInfoId(String selectedRoleInfoId) {
		this.selectedRoleInfoId = selectedRoleInfoId;
	}



	public ArrayList getFacilityIds() {
		return facilityIds;
	}



	public void setFacilityIds(ArrayList facilityIds) {
		this.facilityIds = facilityIds;
	}



	public String getRegType() {
		return regType;
	}



	public void setRegType(String regType) {
		this.regType = regType;
	}



	public String getRegNotificationComments() {
		return regNotificationComments;
	}



	public void setRegNotificationComments(String regNotificationComments) {
		this.regNotificationComments = regNotificationComments;
	}


	public String getStrFacilityIds()
	{
		return this.strFacilityIds;
	}
	
	public void setStrFacilityIds(String strFacilityIds)
	{
		this.strFacilityIds = strFacilityIds;
	}



	public boolean isEnableAddRoleLink() {
		return enableAddRoleLink;
	}



	public void setEnableAddRoleLink(boolean enableAddRoleLink) {
		this.enableAddRoleLink = enableAddRoleLink;
	}



	public String getAffiliation() {
		return affiliation;
	}



	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}



	public String getFacilityList() {
		return facilityList;
	}



	public void setFacilityList(String facilityList) {
		this.facilityList = facilityList;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getTownAndCountyList() {
		return townAndCountyList;
	}



	public void setTownAndCountyList(String townAndCountyList) {
		this.townAndCountyList = townAndCountyList;
	}
	
	
}
