package gov.epa.owm.mtb.cwns.userregistration;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.service.UserService;

public class RegistrationForm extends ActionForm {

	public static final String PUBLIC_USER     = "public_user";
	public static final String CWNS_USER       = "cwns_user";
	public static final String PORTAL_USER     = "portal_user";

	private String action = "";
	private String cwnsUserId;
	
	private boolean actionComplete = false;
	private String newCwnsUserId;
	private String paramResponse;
	private String iamReturn;
	private ActionErrors errors = new ActionErrors();
	
	private String firstName;
	private String lastName;
	private String oidUserId;
	private String userTypeId="xx";
	private String access;
	private String[] states;
	private String[] roles;
	private String facilities;
	private String comments;
	private String emailAddress; 
	private String street="";
	private String city="";
	
	private String affiliation="";
	private String title="";
	private String facilityList="";
	private String townAndCountyList="";
	private Boolean surveyFeedback=new Boolean(false);
	private Boolean utilFeedback=new Boolean(false);
	private Boolean userManagement=new Boolean(false);
	
	private String stateId = "xx";
	private String facilityStateId = "xx";
	
	private String zip="";
	private String phone;
	private String updateable = "true";
	private String cwns2008Home = "";
	private String mode="";
	
	private String  portalRegId;
	
	private String requestId;
	private String requestedGroups;
	

	public String getRequestedGroups() {
		return requestedGroups;
	}

	public void setRequestedGroups(String requestedGroups) {
		this.requestedGroups = requestedGroups;
	}

	public String getUpdateable() {
		return updateable;
	}

	public void setUpdateable(String updateable) {
		this.updateable = updateable;
	}

	// clear Portal Registration data
	public void clearFormData(HttpServletRequest request, UserService userService) {
		setAction("");
		setFirstName("");
		setLastName("");
		setEmailAddress(""); 
		setStreet("");
		setCity("");
		setStateId("xx");
		setFacilityStateId("XX");
		setUserTypeId("xx");
		setZip("");
		setPhone("");
		setOidUserId("");
		setCwnsUserId("");
		setComments("");
		setUpdateable("true");
		setRequestId("");
		setActionComplete(false);
		setNewCwnsUserId("");
		setIamReturn("");
		setParamResponse("");
		setErrors(new ActionErrors());
		setTitle("");
		setAffiliation("");
		setFacilityList("");
		setTownAndCountyList("");
		setUserManagement(new Boolean(false));
		setUtilFeedback(new Boolean(false));
		setSurveyFeedback(new Boolean(false));
		
		if (userService.isPortalRegistration(request)) {
			setMode(PORTAL_USER);
		} else 	if (userService.isPublicUser(request)) {
			setMode(PUBLIC_USER);
		} else {
			setMode(CWNS_USER);
		}
		
		request.getSession().setAttribute(PUBLIC_USER, null);
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getFacilities() {
		return facilities;
	}

	public void setFacilities(String facilities) {
		this.facilities = facilities;
	}

	public String[] getStates() {
		return states;
	}

	public void setStates(String[] states) {
		this.states = states;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String getOidUserId() {
		return oidUserId;
	}

	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}

	public String getCwnsUserId() {
		return cwnsUserId;
	}

	public void setCwnsUserId(String cwnsUserId) {
		this.cwnsUserId = cwnsUserId;
	}

	public String getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(String userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getCwns2008Home() {
		return cwns2008Home;
	}

	public void setCwns2008Home(String cwns2008Home) {
		this.cwns2008Home = cwns2008Home;
	}

	public String getFacilityStateId() {
		return facilityStateId;
	}

	public void setFacilityStateId(String faciltityStateId) {
		this.facilityStateId = faciltityStateId;
	}

	public String getPortalRegId() {
		return portalRegId;
	}

	public void setPortalRegId(String portalRegId) {
		this.portalRegId = portalRegId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public boolean isActionComplete() {
		return actionComplete;
	}

	public void setActionComplete(boolean actionComplete) {
		this.actionComplete = actionComplete;
	}

	public String getIamReturn() {
		return iamReturn;
	}

	public void setIamReturn(String iamReturn) {
		this.iamReturn = iamReturn;
	}

	public String getNewCwnsUserId() {
		return newCwnsUserId;
	}

	public void setNewCwnsUserId(String newCwnsUserId) {
		this.newCwnsUserId = newCwnsUserId;
	}

	public String getParamResponse() {
		return paramResponse;
	}

	public void setParamResponse(String paramResponse) {
		this.paramResponse = paramResponse;
	}

	public ActionErrors getErrors() {
		return errors;
	}

	public void setErrors(ActionErrors errors) {
		this.errors = errors;
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

	public Boolean getSurveyFeedback() {
		return surveyFeedback;
	}

	public void setSurveyFeedback(Boolean surveyFeedback) {
		this.surveyFeedback = surveyFeedback;
	}

	public Boolean getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(Boolean userManagement) {
		this.userManagement = userManagement;
	}

	public Boolean getUtilFeedback() {
		return utilFeedback;
	}

	public void setUtilFeedback(Boolean utilFeedback) {
		this.utilFeedback = utilFeedback;
	}



	
}