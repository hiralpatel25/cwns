package gov.epa.owm.mtb.cwns.npdes;

import java.util.Collection;

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
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class NPDESPermitInfoAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
		 HttpSession pSession = request.getSession(false);
		 CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);
        //	get the facility ID from the page parameter - configured as input parameter in provider.xml
		Long facilityId = new Long(0);
		String permitNbr = "";
		if (prr != null){
			
		if(prr.getParameter("facilityId")!=null) facilityId = new Long(prr.getParameter("facilityId"));
        //	get the Permit Number from the page parameter - configured as input parameter in provider.xml
		
		if(prr.getParameter("permitNumber")!=null) permitNbr = prr.getParameter("permitNumber");
		}
		
		Collection assFacilities = facilityPermitService.facilitiesAssWithPermit(permitNbr, facilityId);
		Object NPDESPermit = facilityPermitService.getNPDESPermitDetails(permitNbr);
		request.setAttribute("facPermits", assFacilities);
		request.setAttribute("permit", NPDESPermit);
		request.setAttribute("isLocalUser", currentUser.isNonLocalUser()?"false":"true");
		return mapping.findForward("success");
	}
	
//  set the facility permit service
    private FacilityPermitService facilityPermitService;
        
	public void setFacilityPermitService(FacilityPermitService facilityPermitService) {
		this.facilityPermitService = facilityPermitService;
	}
	   

}
