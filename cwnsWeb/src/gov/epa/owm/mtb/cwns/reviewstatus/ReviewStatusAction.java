package gov.epa.owm.mtb.cwns.reviewstatus;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.UserService;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
public class ReviewStatusAction extends CWNSAction {

	public static final String ACTION_SEARCH   = "search";
	public static final String ACTION_DEFAULT  = "default";
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		HttpSession pSession = httpReq.getSession(false);
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }	
        // get the form varaibles
		DynaActionForm  reviewStatusForm = (DynaActionForm)form;
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userrole = user.getCurrentRole();
		String locationId = null;
		/* Determine the action to take. First check the form bean
		     * and then (if necessary) check the request object. */  		    
		 String action = null;
		if (prr.getParameter("action") != null && !"".equals(prr.getParameter("action"))) {
			action = prr.getParameter("action");
		}else {
			if(UserService.LOCATION_TYPE_ID_FEDERAL.equalsIgnoreCase(userrole.getLocationTypeId())){
			  if(pSession.getAttribute("state")!=null && !"".equalsIgnoreCase((String)pSession.getAttribute("state")))
				  locationId = (String)pSession.getAttribute("state");
			  else
				  locationId = "DC";
			}else{
				if(pSession.getAttribute("state")!=null && !"".equalsIgnoreCase((String)pSession.getAttribute("state")))
					locationId = (String)pSession.getAttribute("state");
				else
				    locationId = userrole.getLocationId();
			}	
			action=ACTION_DEFAULT;
    	}
     
		if (ACTION_SEARCH.equals(action)) {
           // Location
			locationId = ((String)reviewStatusForm.get("locationId")).trim();
		}
		// Show states dropdown only for Federal and Regional users
		if(UserService.LOCATION_TYPE_ID_FEDERAL.equalsIgnoreCase(userrole.getLocationTypeId()) || 
				UserService.LOCATION_TYPE_ID_REGIONAL.equalsIgnoreCase(userrole.getLocationTypeId())){
		   	
		   request.setAttribute("showStatesDropdown", "true");
		}   
		else{
			request.setAttribute("showStatesDropdown", "false");
		}	
		Collection reviewStatusRef = reviewStatusRefService
		.findReviewStatusFacilityCountAndSort(userrole.getLocationTypeId(),locationId);
		reviewStatusForm.set("locationId", locationId);
		reviewStatusForm.set("action", action);
		request.setAttribute("revStatus", reviewStatusRef);
		pSession.setAttribute("state", locationId);
        request.setAttribute("reviewStatusForm", reviewStatusForm);
		request.setAttribute("locationRefs", reviewStatusRefService.getLocationRefs(user));
		
			String key ="help."+user.getCurrentRole().getLocationTypeId()+".reviewstatusstatistics";
		    String defaultkey = "help.reviewstatusstatistics"; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		request.setAttribute("helpKey", helpKey);
		return mapping.findForward("success");
	}

	private ReviewStatusRefService reviewStatusRefService;

	public void setReviewStatusRefService(ReviewStatusRefService rst) {
		reviewStatusRefService = rst;
	}
	
}


