package gov.epa.owm.mtb.cwns.usersearch;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction;
import gov.epa.owm.mtb.cwns.userlist.UserListAction;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
public class UserSearchAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

   		PortletRenderRequest prr = (PortletRenderRequest)
   				request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
   		
	    UserSearchForm usf = (UserSearchForm) form;

	    //TODO: Check if provider session works in the portal environment
   		//ProviderSession pSession = prr.getSession();
	    HttpSession pSession = request.getSession(false);
		//ProviderSession pSession = prr.getSession();	    
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }

	    /* Determine the action to take. */  		    
	    String action = null;
  		if (prr.getParameter("action") != null && !"".equals(prr.getParameter("action"))) {
   			action = prr.getParameter("action");
   		}else {
   			
   			action=UserListAction.ACTION_DEFAULT_QUERY;
   		}

  		CurrentUser adminUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);
	    usf.setAdminUserLocationType(adminUser.getCurrentRole().getLocationTypeId());
	    
	    initializeSearchArguments(action, usf, prr);
	    
	    usf.setAction("");   // clear the action
		
		// Get data for jsp Select boxes
		String userType       = adminUser.getCurrentRole().getLocationTypeId();
		request.setAttribute("locationTypeRefs",   userService.getLocationTypeRefs("ALL"));
		request.setAttribute("cwnsUserStatusRefs", userService.getCwnsUserStatusRefs());
		request.setAttribute("userAccessLevels",   userService.getAssignedByAccessLevelRefs(adminUser));
		request.setAttribute("locationRefs",       userService.getLocationRefs(adminUser));

		request.setAttribute("userSearchForm",     usf);
		
		String key ="help."+adminUser.getCurrentRole().getLocationTypeId()+".userSearch";
		String defaultkey = "help.userSearch"; 
		String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		request.setAttribute("helpKey", helpKey);		
		return mapping.findForward("success");
	}

	private void initializeSearchArguments(String action, UserSearchForm usf, PortletRenderRequest prr) {
	    if (UserListAction.ACTION_FIRST.equals(action) || 
		    	UserListAction.ACTION_LAST.equals(action)  ||
		    	UserListAction.ACTION_NEXT.equals(action)  ||
		    	UserListAction.ACTION_PREVIOUS.equals(action)  ||
		    	UserListAction.ACTION_SORT.equals(action) ||
		    	UserListAction.ACTION_USER_DEFAULT_SEARCH.equals(action)
		    	) {
		    	
		    	// Clear the search arguments 
			    usf.setLocationId("all");
			    usf.setLocationType("all");
			    usf.setStatus("A");  // default to Active only
			    String[] accessLevels = {""};
			    usf.setAccessLevelIds(accessLevels);
			    usf.setKeyword("");
			    
		    } else {
		    	// Redisplay the existing arguments
			    if (prr.getParameter("locationId") != null) {
			    	String locationId = prr.getParameter("locationId");
			    	if (locationId.length() < 1) locationId = "all";
				    usf.setLocationId(locationId);
			    }
			    
			    if (prr.getParameter("userType") != null) {
				    String userType = prr.getParameter("userType");
				    usf.setLocationType(userType);
			    }
			    
			    if (prr.getParameter("userStatus") != null) { 
			    	String userStatus = prr.getParameter("userStatus");
			    	if (!"user_status".equalsIgnoreCase(userStatus))
			    		usf.setStatus(userStatus);
			    }
			    
			    if (prr.getParameterValues("AccessLevel") != null) {
			    	String[] accessLevels = prr.getParameterValues("AccessLevel");
				    usf.setAccessLevelIds(accessLevels);
			    }
			    
			    if (prr.getParameter("keyword") != null)  {
			    	String keyword = prr.getParameter("keyword");
				    usf.setKeyword(keyword);
			    }
		    }
		
		
	}
	
    /* set the User service */
    private static UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }
}


