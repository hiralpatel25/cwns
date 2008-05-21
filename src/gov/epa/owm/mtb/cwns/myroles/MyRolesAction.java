package gov.epa.owm.mtb.cwns.myroles;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.facility.FacilityListForm;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.PointOfContact;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PocService;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction;
import gov.epa.owm.mtb.cwns.userlist.UserListAction;
import java.util.ArrayList;
import java.util.Collection;

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
public class MyRolesAction extends CWNSAction {

	public static final String ACTION_SWITCH_ROLES="switch_roles";
	public static final String ACTION_DISPLAY="display";
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	    
	    HttpSession pSession = request.getSession(false);
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }

		MyRolesForm myRolesForm = (MyRolesForm) form;
	    CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);

	    int numberOfRoles = currentUser.getRoles().size();
	    log.debug("number of roles = "+ numberOfRoles);
	    if (numberOfRoles == 1) {
	    	return null;
	    }
	    
	    // Determine the action to take
		String action = ACTION_DISPLAY;
    	if(myRolesForm.getAction() != null && !"".equals(myRolesForm.getAction())){
    		action=myRolesForm.getAction();
    	}

  		if (ACTION_SWITCH_ROLES.equals(action)) {
  			userService.switchRole(currentUser,
  									myRolesForm.getLocationTypeId(),
  									myRolesForm.getLocationId());
  			
  			// Set a Session attribute indicating the user role has been switched.
  			// This is required so the Facility List Portlet knows to create a new search.
   		    pSession.setAttribute("switchRole", "switchRole");

		}

  		request.setAttribute("roles", currentUser.getRoles());
  		request.setAttribute("currentRole", currentUser.getCurrentRole());
		
		return mapping.findForward("success");
	}

	/* set the User service */
    private static UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }
}


