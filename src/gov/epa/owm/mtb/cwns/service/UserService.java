package gov.epa.owm.mtb.cwns.service;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;

import gov.epa.iamfw.webservices.user.User;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocation;
import gov.epa.owm.mtb.cwns.model.CwnsUserStatusRef;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsForm;
import gov.epa.owm.mtb.cwns.userregistration.IAMResponse;
import gov.epa.owm.mtb.cwns.userregistration.RegistrationForm;

/**
 * This class provides business functionality related to user management. 
 * @author Matt Connors
 *
 */
public interface UserService {	

	public final int MAX_RESULTS = 10;

	/* Location Type Ids */
	public static final String LOCATION_TYPE_ID_FEDERAL 			= "Federal";
	public static final String LOCATION_TYPE_ID_REGIONAL 			= "Regional";
	public static final String LOCATION_TYPE_ID_STATE 				= "State";
	public static final String LOCATION_TYPE_ID_LOCAL 				= "Local";
	
	
	
	public static final String SELF_SERVICE_REGISTRATION 			= "self";
	public static final String ADMINISTRATOR_MANAGED_REGISTRATION 	= "admin";
	public static final String PORTAL_REGISTRATION 				= "portal";
	public static final String GROUP_SUBSCRIPTION				= "group";
	public static final String ACCOUNT_UPDATE					= "update";
	
	public void saveCwnsUser(CwnsUser cwnsUser, String userId);
	
	public CurrentUser createUserObject(String userId);

	public String findUserNameByUserId(String userId);
	
	public Set getUserIds(Search search);

	public Search getDefaultSearch(CurrentUser user, SortCriteria sortCriteria, int startIndex, int maxResults);		

	public Collection getUserListHelpers(Collection userList, Search search);
	
	public Collection getCwnsUserStatusRefs();
	
	public CwnsUserStatusRef getCwnsUserStatusRef(String cwnsUserStatusId);
	
	public Collection getAssignedByAccessLevelRefs(CurrentUser user);
	
	public CwnsUser findUserByUserId(String userId);	
	
	public UserDetailsForm getUserData(UserDetailsForm udForm, CurrentUser currentUser);	
	
	public Collection getLocationTypeRefs(String userType);
	
	public Collection getLocationRefs();
	public Collection getLocationRefs(CurrentUser adminUser);
	
	public void addRole(UserDetailsForm udForm, String adminUserId);
	
	public void getAccessLevelsForRole(UserDetailsForm udForm);	
	
	public Collection getSelAccessLevels(UserDetailsForm udForm);
	
	public void updateUserData(UserDetailsForm udForm);
	
	public void updateAccessLevels(UserDetailsForm udForm, String adminUserId);
	
	public void deleteRole(UserDetailsForm udForm);
	
	public User regGetPortalUserObject(String oidUserName);
	
	public boolean portalUserExistsInCwns(String oidUserName);
	
	public Search getUserSearch(CurrentUser adminUser, Map queryProperties,  SortCriteria sortCriteria, int startIndex, int maxResults);

	public Collection filterAccessLevelsRefs(Collection userAccessLevels, String locationTypeId);
	
	public boolean isPrimaryRole(UserDetailsForm udForm);
	
	public Collection filterLocationRefs(Collection locationRefs, String locationTypeId);

	public CwnsUserLocation getCwnsUserLocation(String cwnsUserId, String locationTypeId, String locationId);

	public CwnsUser regCreateCWNSUserFromPortalProfile(User user, String adminUserId);
	
	public Collection getFacilitiesForRole(UserDetailsForm udForm);
	
	//public ArrayList getFacilitiesByIds(ArrayList ids);
	
	public ArrayList getFacilitiesByIds(String cwnsUserId,String locationTypeId, String locationId);
	
	public void updateSelectedFacIds(UserDetailsForm udForm,String adminUserId);
	
	
	public ActionErrors regRequestNewPortalAccount(RegistrationForm rForm);
	
	public ActionErrors regApprovePortalUserAccount(String cwnsUserId,
			 										String currentUserId);
	
	public IAMResponse regApprovePortalAccount(String requestId);
	
	public ActionErrors regDenyUserAccount(String cwnsUserId,
			 									 String currentUserId);
	
	public IAMResponse regAddUserToGroup(String portalUserId, String portalGroup);
	
	public IAMResponse regRemoveUserFromGroup(String portalUserId, String portalGroup);
	
	public boolean isPublicUser(HttpServletRequest request);	

	public boolean isPortalRegistration(HttpServletRequest request);	
	
	public boolean isLocationValid(String locationTypeId, String locationId);

	public CwnsUser regCreateCwnsUserFromForm(RegistrationForm rForm, String currentUserId);

	public ActionErrors regUpdateFormWithPortalUserInfo(RegistrationForm rForm, String oidUserId);
	
	public ActionErrors regUpdateFormWithCwnsUserInfo(RegistrationForm rForm,String cwnsUserId);	
	
	public void updateRole(UserDetailsForm udForm, CurrentUser currentUser);
	
	public void regUpdateCwnsObjWithPortalId(String cwnsUserId, String portalUserId, String currentUser);
	
	public void regUpdateCwnsObjFromForm(String cwnsUserId,	RegistrationForm rForm, String currentUserId);

	public int regGetNumberOfPendingUsers(CurrentUser currentUser);	
	
	public void switchRole(CurrentUser currentUser,String locationTypeId,String locationId);
	
	public void regDeleteUser(RegistrationForm rForm, String currentUser);
	
	public void regSuccessfulRegistration(RegistrationForm rForm, String currentUserId, HttpServletRequest request);
	
	public Collection getUserLocationRestrictedFacilitiesId(Long facilityId, String cwnsUserId, String locationTypeId, String locationId);
	
	public void regUpdateFormWithRequestId(RegistrationForm rForm, String requestId);
	
	public  IAMResponse regGetSelfRegistrationPendingRequest(ArrayList selfRegRequests);
	
	public  IAMResponse regGetGroupSubscriptionPendingRequest(ArrayList groupSubRequests);
	
	public void regUpdateCwnsUserAccountRequestId(String requestId,String cwnsUserId);
	
	public IAMResponse regHandleGroupSubscriptionRequest(String requestId, boolean approve);
	
	public ActionErrors regApproveGroupSubscriptionRequest(String cwnsUserId, String currentUserId);
	
	public ActionErrors regDenyGroupSubscriptionRequest(String cwnsUserId, String currentUserId);
	
	public ActionErrors processErrors(IAMResponse portalResponseReq,ActionErrors errors);
	
	//public boolean isRequestSelfRegistration(String requestId);
	
	//public boolean isRequestGroupSubscription(String requestId);
	
	public void setCWNSPrimaryOidGroup(String cwnsUserId, String userType);
	
	public void removeUserFromCWNSGroups(String cwnsUserId);
	
	public void addUserToCWNSGroups(String cwnsUserId, String userType);
	
	public boolean isPortalUserMemberOfCWNSGroup(String oidUserId);
	
	public void getPortalUserGroupMembership(ArrayList memberGroups, String oidUserId);
	
	public int adminGetNumberOfPendingUsers(CurrentUser currentUser);
	
	public void regSuccessfulRegistration(RegistrationForm rForm, String currentUserId, String type);
	
	public Collection getLocationRefs(String locationTypeId);
}
