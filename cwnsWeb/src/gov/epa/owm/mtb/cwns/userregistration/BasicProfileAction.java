package gov.epa.owm.mtb.cwns.userregistration;

/**
 * This class prepares, and processes, the Portal Registration Portlet information.
 * @author Matt Connors
 * @version 1.0
 */
import gov.epa.iamfw.webservices.user.AccountInfo;
import gov.epa.iamfw.webservices.user.ContactInfo;
import gov.epa.iamfw.webservices.user.User;
import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.MailService;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;

import java.util.Collection;
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
import org.apache.struts.action.ActionMessages;


public class BasicProfileAction extends CWNSAction {

	public static final String ACTION_DISPLAY_FORM  = "display_form";
	public static final String ACTION_PROCESS       = "process_basic_profile";
	public static final String DISPLAY_ONLY       = "display_only";
	public static final String ACTION_PROCESS_IAM_REQUEST = "process_iam_request";
	
	public static final String RETURN_TO_SELFREGISTION = "selfregistration";
	public static final String RETURN_TO_SUBSCRIPTION = "subscription";
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
		log.debug("---BasicProfileAction---");
		RegistrationForm rForm = (RegistrationForm) form; 
        ActionErrors errors    = new ActionErrors();
		CwnsUser cwnsUser      = null;
		String nextScreen      = "basicProfile";
		
   		PortletRenderRequest prr = (PortletRenderRequest)
   				request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
   		CurrentUser adminUser = (CurrentUser) request.getSession().getAttribute(CurrentUser.CWNS_USER);
   		String action = getAction(prr, request, rForm);
    	log.debug("action = " + action);
    	log.debug("action = " + action);
    	// User pressed the "back" button
    	if (RegistrationPreliminaryAction.ACTION_PRELIM_REDISPLAY.equals(action)) {
    		log.debug("back button was pressed");
        	return mapping.findForward("preliminaryUserInfo");
		}    	

    	// Get data for jsp Select boxes
		request.setAttribute("states",   facilityService.getStates());
		request.setAttribute("rForm", rForm);

		String  currentUserId;
		boolean publicUser 		 = userService.isPublicUser(request);
		boolean hasPortalAccount = rForm.getOidUserId().length()  > 0;
		boolean hasCwnsAccount   = rForm.getCwnsUserId().length() > 0;
		
		System.out.println("hasPortalAccount = " + hasPortalAccount);
		System.out.println("hasCwnsAccount = " + hasCwnsAccount);
		
		System.out.println("rForm.isActionComplete() = " + rForm.isActionComplete());
		
	    boolean okToUpdate = isTokenValid(request);
	    log.debug("okToUpdate = "+okToUpdate);
		
		if (publicUser) {
			currentUserId = "newUser";
		} else {
		  	currentUserId = adminUser.getUserId();
		}

		verifyUserType(rForm);
		
		if (ACTION_DISPLAY_FORM.equals(action)) {
			request.setAttribute("mode",null);			
			if (hasPortalAccount && !hasCwnsAccount) {
				// Populate the fForm from the Portal User account info
				errors = userService.regUpdateFormWithPortalUserInfo(rForm,rForm.getOidUserId());
				
			} else if (hasPortalAccount && hasCwnsAccount) {
				// Populate the fForm from the Portal User account info
				errors = userService.regUpdateFormWithPortalUserInfo(rForm,rForm.getOidUserId());
				
			} else if (!hasPortalAccount && hasCwnsAccount) {
				// Populate the rForm based on the CWNS User account info
				errors = userService.regUpdateFormWithCwnsUserInfo(rForm,rForm.getCwnsUserId());
				
			} else {
				// User has neither a CWNS or Portal account - nothing to do!
			}
			
			if (!hasPortalAccount) {
				rForm.setUpdateable("true");
			} else {
				rForm.setUpdateable("false");
			}
			
		} else  if (ACTION_PROCESS.equals(action)) {  
			

			System.out.println("Acction_process");
			String type = "";
			if(!rForm.isActionComplete()){
				//Try to create portal acount first
				if (!hasPortalAccount) {	// Create the Portal user account
					type = userService.SELF_SERVICE_REGISTRATION;
					if(!rForm.isActionComplete()){
						errors = userService.regRequestNewPortalAccount(rForm);
					}
					System.out.println("Requestid = " + rForm.getRequestId());
				}else if(hasPortalAccount){
					type = userService.ACCOUNT_UPDATE;
				}

				//if the user does not have CWNS account and there is no error in the condition for creating portal account
				if (!hasCwnsAccount && errors.isEmpty()) {  	// Create the CWNS user account
					if(!rForm.isActionComplete()){ //prevent multiple account creation upon refresh
						cwnsUser = userService.regCreateCwnsUserFromForm(rForm,currentUserId);
						rForm.setActionComplete(true);
					}
				} else if(hasCwnsAccount && errors.isEmpty()){					// Update the CWNS user account with screen data
					System.out.println("Update CWNS Obj");
					userService.regUpdateCwnsObjFromForm(rForm.getCwnsUserId(),	rForm, currentUserId);
				}

				if (hasPortalAccount && hasCwnsAccount) {
					// Update the CWNS object with the Portal UserId
					type = userService.ACCOUNT_UPDATE;
					userService.regUpdateCwnsObjWithPortalId(rForm.getCwnsUserId(),
							rForm.getOidUserId(),
							currentUserId);
					//System.out.println("User has both accounts set request to null");
					// CwnsUser user = userService.findUserByUserId(rForm.getCwnsUserId());
					//user.setPortalRequestId("");
					//userService.saveCwnsUser(user, currentUserId);

				}

				if (errors.isEmpty()) {
					//if(type.equalsIgnoreCase(userService.ACCOUNT_UPDATE)){
					rForm.setNewCwnsUserId(rForm.getCwnsUserId());
					userService.regSuccessfulRegistration(rForm, currentUserId, type);
					//}else{
					//userService.regSuccessfulRegistration(rForm,currentUserId,request);
					//}
					if (publicUser){
						mailService.notifyUserRequestReceived(rForm.getCwnsUserId());
						nextScreen = "thankYou";
					} else {
						nextScreen = "userDetails";
						request.setAttribute("action", UserDetailsAction.ACTION_DISPLAY_SELECTED_USER_INFO);
						prr.setAttribute("cwnsUserId",rForm.getCwnsUserId());
					}
					rForm.clearFormData(request, userService);
					rForm.setAction(null);

				} else {
					// Change the cwnsUser object to inactive (logically delete it)
					//userService.regDeleteUser(rForm, currentUserId);
					rForm.setErrors(errors);
					saveErrors(request, errors);
				}
			}
			if(rForm.isActionComplete()){
				if (errors.isEmpty()) {
					if (publicUser){
						nextScreen = "thankYou";
					} else {
						nextScreen = "userDetails";
						request.setAttribute("action", UserDetailsAction.ACTION_DISPLAY_SELECTED_USER_INFO);
						prr.setAttribute("cwnsUserId",rForm.getNewCwnsUserId());
					}
					rForm.clearFormData(request, userService);
					rForm.setAction(null);
				}else{
					saveErrors(request, rForm.getErrors());
				}
			}
			rForm.setActionComplete(true); //action complete
		}else if(ACTION_PROCESS_IAM_REQUEST.equalsIgnoreCase(action)){
			nextScreen="basicProfile";
			String type = "";
			
			if(rForm.getRequestId() != null && !rForm.isActionComplete()){
				if(rForm.getRequestedGroups() != null && rForm.getRequestedGroups().length() > 0){
					log.debug("update form with oiduserid = " + rForm.getOidUserId());
					//if requestedgroups is not null then the request came from Group Subscription
					//so return it to subscription page
					rForm.setIamReturn(RETURN_TO_SUBSCRIPTION);
					type = userService.GROUP_SUBSCRIPTION;
					//with all requested groups
					rForm.setParamResponse(rForm.getRequestedGroups());
					//use session oid uid to populate form
					errors = userService.regUpdateFormWithPortalUserInfo(rForm, rForm.getOidUserId());
					
					
					
				}else {
					log.debug("update form with requestId");
					userService.regUpdateFormWithRequestId(rForm, rForm.getRequestId());
					type = userService.SELF_SERVICE_REGISTRATION;
					//else the reuqest came from self-registration, use user id to populate rForm
					rForm.setIamReturn(RETURN_TO_SELFREGISTION);
					//nothing to return except error
					rForm.setParamResponse("");
					//use the requestId to update form
					rForm.setOidUserId("");
					hasPortalAccount = false;
				}
				
				
				
				if (!hasCwnsAccount && errors.isEmpty()) {  	// Create the CWNS user account
					cwnsUser = userService.regCreateCwnsUserFromForm(rForm,currentUserId);
					
				} else if(hasCwnsAccount){					// Update the CWNS user account with screen data
					userService.regUpdateCwnsObjFromForm(rForm.getCwnsUserId(),	rForm, currentUserId);
				}
				
				
				if(rForm.getCwnsUserId() != null){
					if(!rForm.isActionComplete()){
						userService.regUpdateCwnsUserAccountRequestId(rForm.getRequestId(), rForm.getCwnsUserId());
					}
				}else{
					log.error("Error unable to update CWNS user account with requestId, Cause CwnsUserId is null");
				}
				
				if (hasPortalAccount && hasCwnsAccount) {
					// Update the CWNS object with the Portal UserId
					 userService.regUpdateCwnsObjWithPortalId(rForm.getCwnsUserId(),
							 								  rForm.getOidUserId(),
							 								  currentUserId);

					 /*System.out.println("User has both accounts set request to null");

					 CwnsUser user = userService.findUserByUserId(rForm.getCwnsUserId());
					 user.setPortalRequestId("");
					 userService.saveCwnsUser(user, currentUserId);*/
					 
				}
				
				if (errors.isEmpty() && rForm.getCwnsUserId() != null) {
					userService.regSuccessfulRegistration(rForm, currentUserId, type);
					if (publicUser || rForm.getIamReturn().equalsIgnoreCase(RETURN_TO_SUBSCRIPTION)){
						mailService.notifyUserRequestReceived(rForm.getCwnsUserId());
					}
				}else{
					rForm.setParamResponse(setResponseParamWithError(rForm.getRequestedGroups()));
				}
				
			}
			
			request.setAttribute("response", rForm.getParamResponse());
			request.setAttribute("return", rForm.getIamReturn());
			System.out.println("rForm.getParamResponse() = " + rForm.getParamResponse());
			System.out.println("rForm.getIamReturn() = " + rForm.getIamReturn());
			rForm.setActionComplete(true); //action complete
		}
		String helpKey = "";
		if(adminUser!=null){
			String key ="help."+adminUser.getCurrentRole().getLocationTypeId()+".registrationportal";
		    String defaultkey = "help.registrationportal"; 
		    helpKey = CWNSProperties.getKey(key, defaultkey); 
						
		}
		else helpKey = "help.registrationportal";
		
		request.setAttribute("helpKey", helpKey);
		saveToken(request);
    	return mapping.findForward(nextScreen);
	}

	/**
	 * Determine what the requested action is.
	 * @param prr
	 * @param request
	 * @param rForm
	 * @return
	 */
	private String getAction(PortletRenderRequest prr,HttpServletRequest request, RegistrationForm rForm) {
   		String action = ACTION_DISPLAY_FORM;
		
	    /* Determine the action to take. First check the form bean
	     * and then (if necessary) check the request object. */  		    
    	if(rForm.getAction() != null && !"".equals(rForm.getAction())){
    		action=rForm.getAction().trim();
    	}else {
    		if (prr.getParameter("action") != null && !"".equals(prr.getParameter("action"))) {
    			action = prr.getParameter("action");
    		}
    	}

    	// If there is a mode attribute use it to determine the action to take.
    	// This is required because the Struts "Mapping.findForward" appears to 
    	// reset all the form bean values.
		if (request.getAttribute("mode") != null) {
			action = (String) request.getAttribute("mode");
			log.debug("mode attribute found, value = " + action );
			request.setAttribute("mode",null);
		}

		return action;
	}
	
	/**
	 * If the user type = "Review" change it to the appropriate value.
	 * @param rForm
	 */
	private void verifyUserType (RegistrationForm rForm) {
		// If the userType is "Review" change to the secific userType
		String userTypeId = rForm.getUserTypeId();
		if ("Review".equals(userTypeId)) {
			if ("HQ".equals(rForm.getStateId())) {
				rForm.setUserTypeId("Federal");
			} else {
				rForm.setUserTypeId("Regional");
			}
		} 
	}
	
	private String setResponseParamWithError(String requestedGroups){
		String groups[] = requestedGroups.split(",");
		String groupsParam = "";
		for(int I = 0; I < groups.length; I++){
			if(groups[I].equalsIgnoreCase(CWNSProperties.getProperty("cwns.iam.group.name"))){
				groups[I] = groups[I] + " : " +  CWNSProperties.getProperty("message.iam.response.failure");
			}
			groupsParam = groups[I] + "," + groupsParam;
		}
		
		if(requestedGroups.length() == 0){
			groupsParam = CWNSProperties.getProperty("message.iam.response.failure");
		}
		
		return groupsParam;
	}
	
    /* set the User service */
    private static UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }

    /* set the facility service */
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }
    
    /* set the Mail service */
    private static MailService mailService;
    public void setMailService(MailService ms){
       mailService = ms;    	
    }

}