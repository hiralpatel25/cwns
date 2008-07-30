package gov.epa.owm.mtb.cwns.manageCostCurve;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.service.ManageCostCurveService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageCostCurveViewAllCatVAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	ManageCostCurveForm manageCostCurveForm = (ManageCostCurveForm) form;
    	
	    PortletRenderRequest prr = (PortletRenderRequest)
		      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		
		//This facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null)
			facId = Long.parseLong(req.getParameter("facilityId"));

		log.debug("req.getParameter(facilityId): " + facId);

		manageCostCurveForm.setManageCostCurveFacilityId(facId);

		ActionForward actionFwd = mapping.findForward("success");

		manageCostCurveForm.setCcCatVHelpers(manageCostCurveService.getCatVCostForFacilitiesInSewershed(facId));

		req.setAttribute("manageCostCurveForm", manageCostCurveForm);
	
		return actionFwd;
	}

	private ManageCostCurveService manageCostCurveService;

	public void setManageCostCurveService(ManageCostCurveService ss) {
		manageCostCurveService = ss;
	}
    private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
}
