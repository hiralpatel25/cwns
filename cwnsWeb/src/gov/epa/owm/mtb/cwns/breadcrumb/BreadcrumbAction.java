package gov.epa.owm.mtb.cwns.breadcrumb;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class BreadcrumbAction extends CWNSAction {

	private Logger log = Logger.getLogger(this.getClass());

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		BreadcrumbForm breadcrumbform = (BreadcrumbForm) form;
		PortletRenderRequest prr = (PortletRenderRequest) request
				.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

		// Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);

		// get the facility Id
		String facilityId = null;
		if (prr != null && prr.getParameter("facilityId") != null) {
			facilityId = prr.getParameter("facilityId");
		} else if (request.getParameter("facilityId") != null) {
			facilityId = request.getParameter("facilityId");
		}
		log.debug("prr.getParameter: " + facilityId);

		// Page Name
		String pageName = null;
		if (prr != null && prr.getParameter("pageName") != null) {
			pageName = prr.getParameter("pageName");
		} else if (request.getParameter("facilityId") != null) {
			pageName = request.getParameter("pageName");
		}
		log.debug("Page Name " + pageName);

		if (pageName != null) {
			request.setAttribute("pageName", pageName);
		}

		if (facilityId != null && (new Long(facilityId)).intValue()!=0) {
			/* obtain the Facility object */
			Facility facility = facilityService.findByFacilityId(facilityId);
			boolean isFacility = facilityService
					.isFacility(new Long(facilityId));
			breadcrumbform.setCwnsNbr(facility.getCwnsNbr() == null ? ""
					: facility.getCwnsNbr());
			breadcrumbform.setFacilityName(facility.getName() == null ? ""
					: facility.getName());
			request.setAttribute("facility", facility);
			request.setAttribute("isFacility", (isFacility == true) ? "true"
					: "false");
		}
		
		request.setAttribute("currentRole", user.getCurrentRole());
	    
		return mapping.findForward("success");
	}

	// set the facility service
	private FacilityService facilityService;

	public void setFacilityService(FacilityService fs) {
		facilityService = fs;
	}

}
