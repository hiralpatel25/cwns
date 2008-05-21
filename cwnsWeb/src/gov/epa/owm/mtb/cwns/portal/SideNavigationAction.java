package gov.epa.owm.mtb.cwns.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.service.MailService;
import gov.epa.owm.mtb.cwns.service.UserService;

public class SideNavigationAction extends CWNSAction{

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		
		PortletRenderRequest prr = (PortletRenderRequest)
			request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
		String requestId = prr.getParameter("request_Id");
		String userId = prr.getParameter("user_Id");
   		String requestedGroups = prr.getParameter("requestedGroups");
		
		System.out.println("userId = " + userId);
		System.out.println("requestId = " + requestId);
		System.out.println("requestedGroups = " + requestedGroups);
		
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		//check to see if user has unsubscribe from CWNS.
		//The checkIAMUnsubscribe param must be set manually to a string > 0
		
		//a cwns subscription has occur
		if(requestedGroups != null 
   				&& requestedGroups.length() > 0
   				&& userId != null
   				&& userId.length() > 0
   				&& requestId != null
   				&& requestId.length() > 0){
   			
   			//The user has subscribe to CWNS
   			//check to see if the user_Id exist in cwns
			try{
   			if(userService.portalUserExistsInCwns(userId)){
   				System.out.println("begin setting account");
   				//if yes, get the cwns account
   				CwnsUser cwnsUser = userService.findUserByUserId(user.getUserId());
   				//update the account
   				cwnsUser.setPortalRequestId(requestId);
   				cwnsUser.setCwnsUserStatusRef(userService.getCwnsUserStatusRef("P"));
   				cwnsUser.setRegType(userService.GROUP_SUBSCRIPTION);
   				userService.saveCwnsUser(cwnsUser, user.getUserId());
   				mailService.notifyUserRequestReceived(cwnsUser.getCwnsUserId());
   				request.setAttribute("requestedGroups", requestedGroups);
   				System.out.println("End Setting account");
   			}
   			
			}catch(Exception e){
				log.error("Error unable to perform subscription for Side Navigation",e);
				e.printStackTrace();
			}
   			
   		}
		
	return mapping.findForward("success");
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

