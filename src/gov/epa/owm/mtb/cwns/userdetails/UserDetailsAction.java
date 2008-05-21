package gov.epa.owm.mtb.cwns.userdetails;

import gov.epa.iamfw.webservices.user.User;
import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.service.MailService;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.userlist.UserListAction;
import gov.epa.owm.mtb.cwns.userregistration.IAMResponse;
import gov.epa.owm.mtb.cwns.userregistration.RegistrationPreliminaryAction;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
public class UserDetailsAction extends CWNSAction {

	public static final String ACTION_UPDATE_USER_INFO  = "update_user_info";
	public static final String ACTION_DISPLAY_USER_INFO = "display_user_info";
	public static final String 
					  ACTION_DISPLAY_SELECTED_USER_INFO = "display_selected_user_info";
	public static final String ACTION_DISPLAY_ROLE      = "display_role";  // edit role
	public static final String ACTION_DELETE_ROLE       = "delete_role";
	public static final String ACTION_DELETE_FACILITY   = "delete_facility";
	public static final String ACTION_UPDATE_ROLE       = "update_role";

	public static final String ACTION_ADD_ROLE          = "add_role";
	public static final String ACTION_NEW_ROLE          = "new_role";
	
	public static final String ACTION_PRELIM_DISPLAY    = "prelim_display";
	public static final String ACTION_PRELIM_PROCESS    = "prelim_process";
	
	public static final String NOTHING_TO_DISPLAY       = "nothing";
	public static final String DISPLAY_USER_INFO        = "display_user_info";
	
	public static final String DISPLAY_ROLE_INFO        = "display_role_info";
	public static final String HIDE_ROLE_INFO           = "hide_role_info";
	
	public static final String ROLE_ADD 				= "role_add";
	public static final String ROLE_UPDATE				= "role_update";
	
	public static final String ACTION_APPROVE_USER      = "approve_user";
	public static final String ACTION_DENY_USER         = "deny_user";
	

	public static final String TRUE                     = "true";
	public static final String FALSE					= "false";
	public static final String VIEW_ONLY				= "View Only";
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

        ActionErrors errors = new ActionErrors();
   		PortletRenderRequest prr = (PortletRenderRequest)
   				request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

	    HttpSession pSession = request.getSession(false);
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }

	    boolean okToUpdate = isTokenValid(request);
	    log.debug("okToUpdate = "+okToUpdate);

	    UserDetailsForm udForm = (UserDetailsForm)form;
	    
		String cwnsUserId = prr.getParameter("cwnsUserId");
		CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);
		
		String key ="help."+currentUser.getCurrentRole().getLocationTypeId()+".userdetails";
	    String defaultkey = "help.userdetails"; 
	    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
	    request.setAttribute("helpKey", helpKey);
		
		if (cwnsUserId == null || cwnsUserId.length() == 0) {
			// Coming from BasicProfile  
			cwnsUserId = (String)prr.getAttribute("cwnsUserId");
		}
			
		String action = getAction(prr, request, udForm);
		log.debug("action = "+action);
		//System.out.println("action = "+action);
	   
		// If necessary forward the request
		if (ACTION_PRELIM_DISPLAY.equals(action) ) {
		   // Go to "add new user" logic
		   return mapping.findForward("newuserpreliminary");
		   
		} else if (UserListAction.ACTION_SEARCH.equals(action) ||
	        UserListAction.ACTION_DEFAULT_QUERY.equals(action) ||
	        UserListAction.ACTION_USER_DEFAULT_SEARCH.equals(action) ||
	        RegistrationPreliminaryAction.ACTION_CANCEL.equals(action)) {

			// Nothing to display
			udForm.setDisplayData(NOTHING_TO_DISPLAY);
			request.setAttribute("userDetailsForm", udForm);
			return mapping.findForward("success");
		}
	    
		// Get data for jsp Select boxes
		String userType       = currentUser.getCurrentRole().getLocationTypeId();
		request.setAttribute("cwnsUserStatusRefs", userService.getCwnsUserStatusRefs());
		request.setAttribute("locationTypeRefs",   userService.getLocationTypeRefs(userType));
		
		
		// set up user access level lists
		Collection userAccessLevels    = userService.getAssignedByAccessLevelRefs(currentUser);
		Collection federalAccessLevels = 
				   userService.filterAccessLevelsRefs(userAccessLevels,
					  								  UserService.LOCATION_TYPE_ID_FEDERAL);
		Collection regionalAccessLevels = 
			   	   userService.filterAccessLevelsRefs(userAccessLevels,
			   			   							  UserService.LOCATION_TYPE_ID_REGIONAL);
		Collection stateAccessLevels = 
		   	       userService.filterAccessLevelsRefs(userAccessLevels,
			  								  		  UserService.LOCATION_TYPE_ID_STATE);
		Collection localAccessLevels = 
		   	       userService.filterAccessLevelsRefs(userAccessLevels,
			  								          UserService.LOCATION_TYPE_ID_LOCAL);
		
		request.setAttribute("userAccessLevels",     userAccessLevels);
		request.setAttribute("federalAccessLevels",  federalAccessLevels);
		request.setAttribute("regionalAccessLevels", regionalAccessLevels);
		request.setAttribute("stateAccessLevels",    stateAccessLevels);
		request.setAttribute("localAccessLevels",    localAccessLevels);
		request.setAttribute("selfacilities",new ArrayList());

		// set up user location lists
		Collection locationRefs = userService.getLocationRefs(currentUser);
		Collection federalLocationRefs = 
  				 userService.filterLocationRefs(locationRefs, UserService.LOCATION_TYPE_ID_FEDERAL);
		Collection regionalLocationRefs = 
				 userService.filterLocationRefs(locationRefs, UserService.LOCATION_TYPE_ID_REGIONAL);
		Collection stateLocationRefs = 
			 userService.filterLocationRefs(locationRefs, UserService.LOCATION_TYPE_ID_STATE);
		Collection localLocationRefs = 
			 userService.filterLocationRefs(locationRefs, UserService.LOCATION_TYPE_ID_LOCAL);
		
		request.setAttribute("locationRefs",         locationRefs);
		request.setAttribute("federalLocationRefs",  federalLocationRefs);
		request.setAttribute("regionalLocationRefs", regionalLocationRefs);
		request.setAttribute("stateLocationRefs",    stateLocationRefs);
		request.setAttribute("localLocationRefs",    localLocationRefs);
		
		// This Collection must allways be present even if it's empty !
		request.setAttribute("facilitiesForRole", new ArrayList());

		request.setAttribute("userDetailsForm", udForm);
		
		// Get selected cwnsUserId and store in the form bean
		udForm.setCwnsUserId(cwnsUserId);
		
		if (ACTION_DISPLAY_USER_INFO.equals(action) ||
			ACTION_DISPLAY_SELECTED_USER_INFO.equals(action) ||	
			UserListAction.ACTION_SEARCH.equals(action)) {
			userService.getUserData(udForm, currentUser);
			udForm.resetRoleData(false);
			udForm.setDisplayData(DISPLAY_USER_INFO);
			//System.out.println("udForm.getStatus() = " + udForm.getStatus());
			
		}else if (ACTION_UPDATE_USER_INFO.equals(action)) {
			// Update user information
			userService.updateUserData(udForm);
			userService.getUserData(udForm, currentUser);
			udForm.resetRoleData(false);

		} else if (ACTION_UPDATE_ROLE.equals(action)) {
			// Process an Update Role request
			userService.updateRole(udForm, currentUser);
			udForm.resetRoleData(false);

		} else if (ACTION_DISPLAY_ROLE.equals(action)) {
			// Display role information for editing
		
			//System.out.println("udForm.getStatus() = " + udForm.getStatus());
			userService.updateUserData(udForm);
			userService.getUserData(udForm, currentUser);
			userService.getAccessLevelsForRole(udForm);
			Collection sel = userService.getSelAccessLevels(udForm);
			
			userAccessLevels = 	userService.filterAccessLevelsRefs(userAccessLevels,
					  			udForm.getCurrentLocationTypeId());
			request.setAttribute("userAccessLevels", userAccessLevels);

			ArrayList facilities = userService.getFacilitiesByIds(cwnsUserId,udForm.getCurrentLocationTypeId(),udForm.getCurrentLocId());
			request.setAttribute("selfacilities", facilities);
			request.setAttribute("selAccessLevels", sel);
			
			
			
//			Collection facilitiesForRole = userService.getFacilitiesForRole(udForm);
//			request.setAttribute("facilitiesForRole", facilitiesForRole);

			udForm.setRoleDisplay(DISPLAY_ROLE_INFO);
			udForm.setRoleMode(ROLE_UPDATE);
			
		} else if (ACTION_DELETE_ROLE.equals(action)) {
			if (userService.isPrimaryRole(udForm)) {
				ActionError error = new ActionError("error.userdetails.primaryrole");
				errors.add(ActionErrors.GLOBAL_ERROR,error);
			}else {
				if (okToUpdate) {
					resetToken(request);
					userService.deleteRole(udForm);
				}
			}
			userService.getUserData(udForm, currentUser);
			udForm.resetRoleData(false);
			
		} else if (ACTION_ADD_ROLE.equals(action)) {
			// Process a new role
			if (userService.getCwnsUserLocation(udForm.getCwnsUserId(),
									   udForm.getCurrentLocationTypeId(),
									   udForm.getCurrentLocId()) == null ) {
				userService.addRole(udForm,currentUser.getUserId());
				userService.updateUserData(udForm);
				userService.getUserData(udForm, currentUser);
				udForm.resetRoleData(false);			
			} else { // The Role already exists
				ActionError error = new ActionError("error.userdetails.roleexists");
				errors.add(ActionErrors.GLOBAL_ERROR,error);
				udForm.setRoleDisplay(HIDE_ROLE_INFO);
				userService.getUserData(udForm, currentUser);
			}
			
		} else if (ACTION_NEW_ROLE.equals(action)) {
			// Prepare the screen to add a new role
			userService.getUserData(udForm, currentUser);
			udForm.resetRoleData(true);
			udForm.setAllFacilities("Y");
			
		} else if (ACTION_APPROVE_USER.equals(action)) {

			boolean approvePortalAccount = false;
			

			String roleInfo [] = udForm.getPrimaryRoleInfo().split(":");
			String locationTypeId = roleInfo[0];

			

			System.out.println("udForm.getRegType() = " + udForm.getRegType());
			System.out.println("locationTypeid = " + locationTypeId);
			
			if(udForm.getRegType().equalsIgnoreCase(userService.SELF_SERVICE_REGISTRATION)){

				log.debug("approve self");
				approvePortalAccount = true;
				userService.updateUserData(udForm);
				errors = userService.regApprovePortalUserAccount(udForm.getCwnsUserId(),currentUser.getUserId());
				if(errors.isEmpty()){

					userService.setCWNSPrimaryOidGroup(udForm.getCwnsUserId(), locationTypeId);

				}
			}else if(udForm.getRegType().equalsIgnoreCase(userService.GROUP_SUBSCRIPTION)){
				//if the ticket is not self-registration, than it is GroupSubscription.
				log.debug("Approve group subscription");
				userService.updateUserData(udForm);
				errors = userService.regApproveGroupSubscriptionRequest(udForm.getCwnsUserId(), currentUser.getUserId());
				if(errors.isEmpty()){

					userService.setCWNSPrimaryOidGroup(udForm.getCwnsUserId(), locationTypeId);

				}
			}
			else {
				log.debug("update user info");
				udForm.setStatus("A");				
				userService.updateUserData(udForm);
				// Add user to the CWNS Portal Community  
				userService.regAddUserToGroup(udForm.getCwnsUserId(),CWNSProperties.getProperty("cwns.groupName"));
				log.debug("add " + udForm.getOidUserId() + " to " + CWNSProperties.getProperty("cwns.groupName"));

				userService.setCWNSPrimaryOidGroup(udForm.getCwnsUserId(), locationTypeId);

			}
			
			if (errors.isEmpty()) {
				mailService.notifyUserOfApproval(udForm.getCwnsUserId(),approvePortalAccount);
				udForm.setDisplayData(NOTHING_TO_DISPLAY);
				resetToken(request);
								
			}else {
				userService.getUserData(udForm, currentUser);
				udForm.resetRoleData(false);
				udForm.setDisplayData(DISPLAY_USER_INFO);
			}
			
		} else if (ACTION_DENY_USER.equals(action)) {
			// Update the CwnsUser object to capture any comments the administrator may
			// have added.
			
			//System.out.println("Deny udForm.getRegType() = " + udForm.getRegType());
			
			if(udForm.getRegType().equalsIgnoreCase(userService.SELF_SERVICE_REGISTRATION)){
				userService.updateUserData(udForm);
			errors = userService.regDenyUserAccount(udForm.getCwnsUserId(),
										            currentUser.getUserId());
			}else if(udForm.getRegType().equalsIgnoreCase(userService.GROUP_SUBSCRIPTION)){
				//System.out.println("Deny User");
				userService.updateUserData(udForm);
				//System.out.println("reject");
				errors = userService.regDenyGroupSubscriptionRequest(udForm.getCwnsUserId(), udForm.getCwnsUserId());
			}
			else{
				CwnsUser cwnsUser = userService.findUserByUserId(udForm.getCwnsUserId());
				udForm.setStatus("I");
				udForm.setOidUserId("");
				userService.updateUserData(udForm);
				cwnsUser.setPortalRequestId("");
				userService.saveCwnsUser(cwnsUser, currentUser.getUserId());
			}
			
			if (errors.isEmpty()) {
				udForm.setDisplayData(NOTHING_TO_DISPLAY);
			    mailService.notifyUserDenied(udForm.getCwnsUserId());				
			}else {
				userService.getUserData(udForm, currentUser);
				udForm.resetRoleData(false);
				udForm.setDisplayData(DISPLAY_USER_INFO);
			}
		}
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		} 
    	log.debug("Time = " + new Date().toString());


	    saveToken(request);
		return mapping.findForward("success");
	}

	/**
     * Determine the action to take. Check things in this order: 
     * 		Form Bean action
     * 		Forwarded from a Struts Action
     * 		Event was thrown
     * 		if none of the above use a default action based
     *         on whether this is a Portal or a Public user.
	 * 
	 * @param prr
	 * @param request
	 * @param udForm
	 * @return
	 */
	private String getAction (PortletRenderRequest prr, 
							  HttpServletRequest request,
							  UserDetailsForm udForm) {
		
	    String action = null;
	    // Check the form bean
    	if(udForm.getAct() != null && !"".equals(udForm.getAct())){
    		action=udForm.getAct().trim();
    		
    	// Check if forwarded from another Struts action
    	}else if (prr.getAttribute("action") != null) {
			action = (String) prr.getAttribute("action");
		
		// Check for an Event
    	} else if (prr.getParameter("action") != null && 
    			   !"".equals(prr.getParameter("action"))) {
   			action = prr.getParameter("action");
   			
   		// Didn't find anything so use the default action
   		}else {
   			if (userService.isPublicUser(request)) {
   				action = ACTION_PRELIM_DISPLAY;
   			} else  {
   				action = UserListAction.ACTION_DEFAULT_QUERY;
   				   				
   			}
   		}		
		
    	return action;
	}
	
	
	
    /* set the User service */
    private static UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }
    
    /* set the Mail service */
    private static MailService mailService;
    public void setMailService(MailService ms){
       mailService = ms;    	
    }
}


