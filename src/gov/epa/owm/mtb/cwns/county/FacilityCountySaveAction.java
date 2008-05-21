package gov.epa.owm.mtb.cwns.county;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

public class FacilityCountySaveAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		FacilityCountyForm facilityCountyForm = (FacilityCountyForm)form;

		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
        //	check if the action is or duplicate request 
		if(isCancelled(request) || !isTokenValid(request, true)){
			log.debug("in cancel---");
			facilityCountyForm.setCountyName("");
			facilityCountyForm.setFipsCode("");
			facilityCountyForm.setPrimary('N');
			facilityCountyForm.setMode("list");
			request.setAttribute("save_mode", "cancel");
			return mapping.findForward("success");
					
		} 
		
		//	 	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
        //	get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(facilityCountyForm.getFacilityId()!=null){
			facilityId = facilityCountyForm.getFacilityId();
		}
		String countyName = facilityCountyForm.getCountyName();
		String fipsCode = facilityCountyForm.getFipsCode();
		String locationId = facilityCountyForm.getLocationId();
		char primary = facilityCountyForm.getPrimary();
		String mode = facilityCountyForm.getMode();
		long countyId = facilityCountyForm.getCountyId();
		long geographicAreaId = facilityCountyForm.getGeographicAreaId();
		
		if("add".equalsIgnoreCase(mode)){
		// check for duplicate county
		GeographicAreaCounty g = facilityAddressService.getGACByCountyNameAndFacilityId(fipsCode, countyName,facilityId);
		if (g != null){
			ActionErrors errors = new ActionErrors();
		    errors.add("county", new ActionError("error.geographicareacounty.countyexists"));
		    saveErrors(request, errors);
		    request.setAttribute("save_mode", "errors");
		    saveToken(request);
		    return mapping.findForward("success");
		}
		
		facilityAddressService.saveFacilityCounty(fipsCode, facilityId,countyName,primary,locationId,user);
		}
		else{
			facilityAddressService.updateGeographicAreaCounty(facilityId,geographicAreaId,countyId,primary,user);
		}
		resetToken(request);
		facilityCountyForm.setMode("list");
		request.setAttribute("save_mode", "success");
		//fesManager.runValidation(facilityId, FacilityService.DATA_AREA_GEOGRAPHIC, user.getUserId());
		facilityService.performPostSaveUpdates(facilityId, FacilityService.DATA_AREA_GEOGRAPHIC, user);
		return mapping.findForward("success");
	}
	
	 //  set the facility address service
    private FacilityAddressService facilityAddressService;
        
	public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	}
	private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
    	facilityService=fs;
    }

}
