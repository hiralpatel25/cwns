package gov.epa.owm.mtb.cwns.navigationTabs;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.NavigationTabsService;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class NavigationTabsAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	NavigationTabsListForm navigationTabsListForm = (NavigationTabsListForm) form;
    	
    	log.debug("Token: " + generateToken(req));
		log.debug(" IN - navigationTabsListForm--> " + navigationTabsListForm.toString());

        //Get user privilages
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userRole = user.getCurrentRole();
		
		//this should be commented out during deployment
		//userRole.setType(UserRole.LOCAL);
		
		String userType = userRole.getLocationTypeId();
		
		navigationTabsListForm.setUserType(userType);
		
	    PortletRenderRequest prr = (PortletRenderRequest)
		      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 

		//The facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null && !req.getParameter("facilityId").equals(""))
			facId = Long.parseLong(req.getParameter("facilityId"));

		log.debug("req.getParameter(facilityId): " + facId);

		//The facilityId comes from portlet parameter
		String navigationTabType = "";
		if(req.getParameter("navigationTabType")!=null && !req.getParameter("navigationTabType").equals(""))
			{
				navigationTabType = req.getParameter("navigationTabType").replaceAll("_AND_", " & ");
				navigationTabType = navigationTabType.replaceAll("_", " ");
				
				if(navigationTabType.equalsIgnoreCase("Needs")) 
				{
					navigationTabsListForm.setHasLink("Y");
					navigationTabsListForm.setLinkText("Capital Costs");
				}
				else if(req.getParameter("navigationTabType").endsWith("_Link"))
				{
					navigationTabsListForm.setLinkText((req.getParameter("navigationTabType").replaceAll("_Link", "")).replaceAll("_", " "));
					navigationTabsListForm.setLinkTabParent("Needs"); // hard code for now; place holder fo rthe future
					navigationTabsListForm.setHasLink("Y");
				}
			}
		
		log.debug("req.getParameter(navigationTabType): " + navigationTabType);
		
		ActionForward actionFwd = mapping.findForward("success");
		
		// if the tab is link type, all top level tabs will be enabled
		Collection navigationTabsListHelpers = 
			navigationTabsService.findValidatedDataAreasByFacilityId(Long.toString(facId), 
					navigationTabType, navigationTabsListForm.getHasLink().equals("Y"), user);

		navigationTabsListForm.setNavigationTabsHelpers(navigationTabsListHelpers);
		navigationTabsListForm.setFacilityID(facId);
		
		navigationTabsListForm.setFacility(faclityService.isFacility(new Long(facId)));
		
		req.setAttribute("navigationTabsForm", navigationTabsListForm);
		
		log.debug(" Out - navigationTabsListForm--> " + navigationTabsListForm.toString());
		
		return actionFwd;
	}

	private NavigationTabsService navigationTabsService;
	private FacilityService faclityService;

	public void setNavigationTabsService(NavigationTabsService ss) {
		navigationTabsService = ss;
	}

	public void setFacilityService(FacilityService fs) {
		faclityService = fs;
	}
	

}
