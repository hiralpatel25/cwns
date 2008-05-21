package gov.epa.owm.mtb.cwns.userregistration;

/**
 * This class prepares, and processes, the Portal Registration Portlet information.
 * @author Matt Connors
 * @version 1.0
 */
import gov.epa.iamfw.webservices.user.User;
import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction;

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

public class RegistrationPreliminaryAction extends CWNSAction {
	
	public static final String ACTION_PROCESS       		= "process_prelim";
	public static final String ACTION_CANCEL        		= "cancel";
	public static final String ACTION_PRELIM_DISPLAY 		= "prelim_display";
	public static final String ACTION_PRELIM_DISPLAY_PUBLIC = "prelim_display_public";
	public static final String ACTION_PRELIM_REDISPLAY 		= "prelim_redisplay";
	
	
	
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
		log.debug("---RegistrationPreliminaryAction---");
		RegistrationForm rForm = (RegistrationForm) form; 
        ActionErrors errors = new ActionErrors();

        String nextScreen = "preliminaryUserInfo";
        CurrentUser user = null;
   		PortletRenderRequest prr = (PortletRenderRequest)
   				request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
   		
   		//   	get IAM param user_Id
   		String userId = prr.getParameter("user_Id");
   		if(userId != null && userId.length() > 0){
   			request.setAttribute("user_id", userId);
   			rForm.setOidUserId(userId);
   			//do not set CWNS number
   		}
   		
   		//get IAM param request_Id
   		String requestId = prr.getParameter("request_Id");
   		if(requestId != null && requestId.length() > 0){
   			rForm.setRequestId(requestId);
   		}
   		
   		//get IAM param requestedGroups
   		String requestedGroups = prr.getParameter("requestedGroups");
   		if(requestedGroups != null && requestedGroups.length() > 0){
   			rForm.setRequestedGroups(requestedGroups);
   		}
   		
   		log.debug("userId = " + userId);
   		log.debug("requestId = " + requestId);
   		log.debug("requestedGroups = " + requestedGroups);
   		
   		//TODO: Check if provider session works in the portal environment
   		//ProviderSession pSession = prr.getSession();
	    HttpSession pSession = request.getSession(false);
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }
	    CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);
	    /* Determine the action to take. First check the form bean
	     * and then (if necessary) check the request object. */  		    
	    String action = null;
    	if(rForm.getAction() != null && !"".equals(rForm.getAction())){
    		action=rForm.getAction().trim();
    	}else {
    		if (prr.getParameter("action") != null && !"".equals(prr.getParameter("action"))) {
    			action = prr.getParameter("action");
    		}else {
    			action=ACTION_PRELIM_DISPLAY_PUBLIC;
    		}
    	}

    	log.debug("action = " + action);
    	
    	
    	if (BasicProfileAction.ACTION_PROCESS.equals(action)) {
    		// This is a process Basic Profile request
    		log.debug("setting mode attribute");
			request.setAttribute("mode",BasicProfileAction.ACTION_PROCESS );
			nextScreen="basicProfileUserInfo";
			return mapping.findForward(nextScreen);
    	}
    	
    	boolean publicUser 		 = userService.isPublicUser(request);
    	if (publicUser) {
    		request.setAttribute("locationRefs", userService.getLocationRefs(userService.LOCATION_TYPE_ID_STATE));
    	}else if (!publicUser) {
    		request.setAttribute("locationRefs", userService.getLocationRefs());
    	}
    	
		request.setAttribute("rForm", rForm);
		
		
		if (ACTION_PRELIM_DISPLAY_PUBLIC.equals(action)){
			request.getSession().setAttribute(RegistrationForm.PUBLIC_USER, "yes");
			rForm.setMode(RegistrationForm.PUBLIC_USER);
			String cwns2008Home = CWNSProperties.getProperty("custom.login.jsp.location");
			rForm.setCwns2008Home(cwns2008Home);
			//set session info
			setSessionUserInfo(request, prr, rForm);
			
			
		} else if (UserDetailsAction.ACTION_PRELIM_DISPLAY.equals(action)) {
			//CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);		  	
			rForm.clearFormData(request, userService);
			rForm.setFacilityStateId(currentUser.getCurrentRole().getLocationId());
			request.getSession().setAttribute(RegistrationForm.PUBLIC_USER, null);
			rForm.setMode(RegistrationForm.PORTAL_USER);
		}
		else if (ACTION_PROCESS.equals(action))  {
			request.setAttribute("mode",null);	
			log.debug("clearing mode attribute");
			String portalUserId = rForm.getOidUserId().trim();
			String cwnsUserId   = rForm.getCwnsUserId().trim();
			
			if (portalUserId.length() > 0 || cwnsUserId.length()> 0) {
				CwnsUser cwnsUser = null;
				
				// Do the following only if requestId is > 0
				if(rForm.getRequestId()== null
					||rForm.getRequestId().length() == 0){
					// If the user entered a Portal userId make sure it exists
					if (portalUserId.length() > 0 ) {
						if (userService.regGetPortalUserObject(portalUserId) == null) {
							// No Portal Profile found
							ActionError error = new ActionError("error.registration.portalUserNotFound",portalUserId);
							errors.add(ActionErrors.GLOBAL_ERROR,error);
						}
					}
				}
				// If the user entered a CWNS userId make sure it exists
				if (cwnsUserId.length() > 0) {
					cwnsUser = userService.findUserByUserId(cwnsUserId);
					if (cwnsUser == null) {
						// No CWNS_USER record found
						ActionError error = new ActionError("error.registration.cwnsUserNotFound",cwnsUserId);
						errors.add(ActionErrors.GLOBAL_ERROR,error);
					}
				}
					
				
				if (cwnsUser != null) { // The CWNS_USER record was retrieved
					
					// See if the CWNS_USER record has a portal account associated with it
					boolean userAlreadyHasPortalAccount = 
						(cwnsUser.getOidUserid() != null && cwnsUser.getOidUserid().length() > 0 ) 
							? true : false;
				
					if (userAlreadyHasPortalAccount) {
						
						if (portalUserId.length() == 0) {
							// The user specified a CWNS userId but not a Portal userId. But, the 
							// CWNS_USER record identifies a Portal userId.
							ActionError error = new ActionError("error.registration.alreadyAssociatedWithPortalAccount",cwnsUserId);
							errors.add(ActionErrors.GLOBAL_ERROR,error);
							
						} else {
							// Both userIs were entered and both already exist 
							if (!portalUserId.equalsIgnoreCase(cwnsUser.getOidUserid())) {
								// The Portal userId entered does not match the one in the CWNS_USER record
								ActionError error = new ActionError("error.registration.associatedWithDifferentPortalAccount",cwnsUserId);
								errors.add(ActionErrors.GLOBAL_ERROR,error);
							}
						}
					}
				} 
				
				// A Portal userId was entered and a CWNS userId was not. Make sure the 
				// Portal userId is not aleady associated with a CWNS_USER record.
				if (portalUserId.length() > 0 || cwnsUserId.length() == 0) {
					if (userService.portalUserExistsInCwns(portalUserId)) {
						// The Portal userId entered is already associated with a CWNS_USER record
						ActionError error = new ActionError("error.registration.portalAccountAlreadyUsed",portalUserId);
						errors.add(ActionErrors.GLOBAL_ERROR,error);
					}
				}
			}

			
			if (cwnsUserId.length() == 0) { // The user is requesting a CWNS account
				
				String userTypeId = rForm.getUserTypeId();
				if ("Review".equals(userTypeId)) {

					// Determine whether the user is requesting Federal or Regional access
					if ("HQ".equals(rForm.getFacilityStateId())) {
						rForm.setUserTypeId("Federal");
					} else {
						rForm.setUserTypeId("Regional");
					}
				} 
				
				if (!userService.isLocationValid(rForm.getUserTypeId(), rForm.getFacilityStateId())) {

					// The userType/userLocation combination is not valid
					ActionError error = new ActionError("error.registration.locationAndLocationTypeCombination");
					errors.add(ActionErrors.GLOBAL_ERROR,error);

					// set the UserTypeId back to the value entered by the user
					rForm.setUserTypeId(userTypeId);
					
				}  else {
					// Use the facility location as the default user location
					rForm.setStateId(rForm.getFacilityStateId());
				}
			}
			nextScreen="basicProfileUserInfo";
			if(requestId == null){
				//if there is no requestId follow normal logic
				request.setAttribute("mode",BasicProfileAction.ACTION_DISPLAY_FORM );
				rForm.setAction(BasicProfileAction.ACTION_DISPLAY_FORM);
			}else{
				//if there is requestId send to BasicProfileAction to be process
				request.setAttribute("mode",BasicProfileAction.ACTION_PROCESS_IAM_REQUEST );
				rForm.setAction(BasicProfileAction.ACTION_PROCESS_IAM_REQUEST);
			}

		} else if (ACTION_CANCEL.equals(action)) {

			if (userService.isPublicUser(request)) {
				nextScreen="Cwns2008Home";
			} else {
				nextScreen="userDetails";
			}
		}
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			nextScreen="preliminaryUserInfo";
		} 
		String helpKey = "";
		if(currentUser!=null){
		    String key ="help."+currentUser.getCurrentRole().getLocationTypeId()+".registrationportal";
		    String defaultkey = "help.registrationportal"; 
		    helpKey = CWNSProperties.getKey(key, defaultkey); 
		}
		else helpKey = "help.registrationportal";
				
		request.setAttribute("helpKey", helpKey);
		return mapping.findForward(nextScreen);
	}

	private void setSessionUserInfo(HttpServletRequest request, PortletRenderRequest prr, RegistrationForm rForm){
		String username = request.getParameter("_username");
   		log.debug("username = " + username);
   		
   		if(username != null && !username.equalsIgnoreCase("PUBLIC") && username.length() > 0){
			rForm.setOidUserId(username);
			request.setAttribute("username", username);
			//do not set CWSNS number
		}
	}
	
    /* set the User service */
    private static UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }
}