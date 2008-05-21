package gov.epa.owm.mtb.cwns.congressionalDistrict;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCongDistrict;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

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

public class CongressionalDistrictSaveAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		CongressionalDistrictForm conDistrictForm = (CongressionalDistrictForm)form;
        //	check if the action is or duplicate request 
		if(isCancelled(request) || !isTokenValid(request, true)){
			log.debug("in cancel---");
			conDistrictForm.setConDistrictName("");
			conDistrictForm.setConDistrictId("");
			conDistrictForm.setPrimary('N');
			conDistrictForm.setMode("list");
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
		}else if(conDistrictForm.getFacilityId()!=null){
			facilityId = conDistrictForm.getFacilityId();
		}
		//String conDistrictName = conDistrictForm.getConDistrictName();
		String conDistrictId = conDistrictForm.getConDistrictId();
		String locationId = conDistrictForm.getLocationId();
		char primary = conDistrictForm.getPrimary();
		String mode = conDistrictForm.getMode();
		long geographicAreaId = conDistrictForm.getGeographicAreaId();
		log.debug("conDistrictId----"+conDistrictId);
		if("add".equalsIgnoreCase(mode)){
		    // check for duplicate congressional district
			GeographicAreaCongDistrict g = facilityAddressService.getGeographicAreaCongDistrict(conDistrictId,facilityId);
		if (g != null){
			ActionErrors errors = new ActionErrors();
		    errors.add("conDistrict", new ActionError("error.geographicareacondistrict.condistrictexists"));
		    saveErrors(request, errors);
		    request.setAttribute("save_mode", "errors");
		    saveToken(request);
		    return mapping.findForward("success");
		}
		
		facilityAddressService.saveGeographicAreaCongDistrict(facilityId,conDistrictId,primary,locationId,user);
		}
		else{
			facilityAddressService.updateGeographicAreaCongDistrict(facilityId,geographicAreaId,conDistrictId,primary,user);
		}
		resetToken(request);
		conDistrictForm.setMode("list");
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
