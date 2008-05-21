package gov.epa.owm.mtb.cwns.boundary;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BoundaryDeleteAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		BoundaryForm boundaryForm = (BoundaryForm)form;
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    	
        // 	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
        //	get the boundary Id
		Long boundaryId = new Long(0);
		if(prr !=null && prr.getParameter("boundaryId")!=null){
			boundaryId = new Long(prr.getParameter("boundaryId"));
		}else if(request.getParameter("boundaryId")!=null){
			boundaryId = new Long(request.getParameter("boundaryId"));
		}
		log.debug("boundaryId=" + boundaryId);
		if(boundaryId == new Long(0)){
			throw new ApplicationException("boundary Id is needed to edit the Geographic Area Boundary");
		}    
		
		//get the GeographicArea Id
		Long geographicAreaId = new Long(0);
		if(prr!=null && prr.getParameter("geographicAreaId")!=null) {
			geographicAreaId = new Long(prr.getParameter("geographicAreaId"));
		}else if(request.getParameter("geographicAreaId")!=null){
			geographicAreaId = new Long(request.getParameter("geographicAreaId"));
		}
		log.debug("GeographicAreaId=" + geographicAreaId);
		if(geographicAreaId == new Long(0)){
			throw new ApplicationException("geographicArea Id is needed to edit the Geographic Area County");
		}
		
		//get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(boundaryForm.getFacilityId()!=null){
			facilityId = boundaryForm.getFacilityId();
		}
		log.debug("facilityId----"+facilityId);
		
		if(facilityService.isFeedBack(facilityId, user)){
			facilityAddressService.softDeleteGeographicAreaBoundary(facilityId,geographicAreaId, boundaryId, user);
			return mapping.findForward("success");
		}
		facilityAddressService.deleteGeographicAreaBoundary(facilityId, geographicAreaId, boundaryId, user);
		//fesManager.runValidation(facilityId, FacilityService.DATA_AREA_GEOGRAPHIC, user.getUserId());
		facilityService.performPostSaveUpdates(facilityId, FacilityService.DATA_AREA_GEOGRAPHIC, user);
		return mapping.findForward("success");	
	}
	
    //  set the facility address service
    private FacilityAddressService facilityAddressService;
        
	public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	}

    // set the facility service
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }
}
