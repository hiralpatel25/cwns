package gov.epa.owm.mtb.cwns.administration;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.userlist.UserListAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AdministrationAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
		
	    //TODO: Check if provider session works in the portal environment
		//ProviderSession pSession = prr.getSession();
	    HttpSession pSession = request.getSession(false);
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }
		 
	    /* User Information  */
	    CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);

	    if (currentUser.isUserManager()) {

	   		// Store the number of Pending users in the HTTP request object
	        int pendingUsers = userService.adminGetNumberOfPendingUsers(currentUser);
	   		prr.setAttribute("pendingUsers", new Integer(pendingUsers));
	    }
	    
	    //Set Attributes if user has more than one role
	    if(currentUser.getRoles().size() > 1){
	    	request.setAttribute("roles", currentUser.getRoles());
	    	request.setAttribute("currentRole", currentUser.getCurrentRole());
	    }
	    String key ="help."+currentUser.getCurrentRole().getLocationTypeId()+".administration";
	    String defaultkey = "help.administration"; 
	    String helpKey = CWNSProperties.getKey(key, defaultkey); 
		request.setAttribute("helpKey", helpKey);
	    
		return mapping.findForward("success");
	}
	
    /* set the User service */
    private UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }
	

}
