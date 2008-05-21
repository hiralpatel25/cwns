package gov.epa.owm.mtb.cwns.congressionalDistrict;

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
import gov.epa.owm.mtb.cwns.county.FacilityCountyForm;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class CongressionalDistrictDeleteAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		CongressionalDistrictForm conDistrictForm = (CongressionalDistrictForm)form;
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    	
        // 	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
        //	get the conDistrict Id
		String conDistrictId = "";
		if(prr !=null && prr.getParameter("conDistrictId")!=null){
			conDistrictId = prr.getParameter("conDistrictId");
		}else if(request.getParameter("conDistrictId")!=null){
			conDistrictId = request.getParameter("conDistrictId");
		}
		log.debug("conDistrictId=" + conDistrictId);
		if(conDistrictId.equals("")){
			throw new ApplicationException("Congressional District Id is needed to edit the Geographic Area Congressional District");
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
		}else if(conDistrictForm.getFacilityId()!=null){
			facilityId = conDistrictForm.getFacilityId();
		}
		log.debug("facilityId----"+facilityId);
		
		if(facilityService.isFeedBack(facilityId, user)){
			facilityAddressService.softDeleteGeographicAreaCongDistrict(facilityId, geographicAreaId, conDistrictId, user);
			return mapping.findForward("success");
		}
		
		facilityAddressService.deleteGeographicAreaCongDistrict(facilityId, geographicAreaId, conDistrictId, user);
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
