package gov.epa.owm.mtb.cwns.watershed;

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
import gov.epa.owm.mtb.cwns.congressionalDistrict.CongressionalDistrictForm;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCongDistrict;
import gov.epa.owm.mtb.cwns.model.GeographicAreaWatershed;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class WatershedSaveAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		WatershedForm watershedForm = (WatershedForm)form;
        // check if the action is or duplicate request 
		if(isCancelled(request) || !isTokenValid(request, true)){
			log.debug("in cancel---");
			watershedForm.setWatershedName("");
			watershedForm.setWatershedId("");
			watershedForm.setPrimary('N');
			watershedForm.setMode("list");
			request.setAttribute("save_mode", "cancel");
			return mapping.findForward("success");
					
		} 
		
		//	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
        //	get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(watershedForm.getFacilityId()!=null){
			facilityId = watershedForm.getFacilityId();
		}
		
		String watershedName = watershedForm.getWatershedName();
		String watershedId = watershedForm.getWatershedId();
		String locationId = watershedForm.getLocationId();
		char primary = watershedForm.getPrimary();
		String mode = watershedForm.getMode();
		long geographicAreaId = watershedForm.getGeographicAreaId();
		
		if("add".equalsIgnoreCase(mode)){
		    // check for duplicate watershed
			GeographicAreaWatershed g = facilityAddressService.getGeographicAreaWatershed(watershedId,facilityId);
		if (g != null){
			ActionErrors errors = new ActionErrors();
		    errors.add("watershed", new ActionError("error.geographicareawatershed.watershedexists"));
		    saveErrors(request, errors);
		    request.setAttribute("save_mode", "errors");
		    saveToken(request);
		    return mapping.findForward("success");
		}
		
		facilityAddressService.saveGeographicAreaWatershed(facilityId,watershedId,primary,locationId,user);
		}
		else{
			facilityAddressService.updateGeographicAreaWatershed(facilityId,geographicAreaId,watershedId,primary,user);
		}
		resetToken(request);
		watershedForm.setMode("list");
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
	
    //  set the facility service
    private FacilityService facilityService;
    
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    } 	

}
