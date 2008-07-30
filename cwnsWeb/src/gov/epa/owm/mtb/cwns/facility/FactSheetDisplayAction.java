package gov.epa.owm.mtb.cwns.facility;

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
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class FactSheetDisplayAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);

		//get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(request.getParameter("facilityId")!=null){
			facilityId = new Long(request.getParameter("facilityId"));
		}
		log.debug("FacilityId=" + facilityId);
		if(facilityId.equals(new Long(0))){
			throw new ApplicationException("Facility Id is needed to delete the facility type");
		}
		
		Facility facility = facilityService.findByFacilityId(facilityId.toString());
		request.setAttribute("facility", facility);
		
		return mapping.findForward("success");
	}
	
	private FacilityService facilityService;

	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
}
