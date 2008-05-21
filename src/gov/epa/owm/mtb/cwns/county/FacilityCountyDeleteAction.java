package gov.epa.owm.mtb.cwns.county;

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
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class FacilityCountyDeleteAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
     
		FacilityCountyForm facilityCountyForm = (FacilityCountyForm)form;
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    	
        // 	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
		//get the county Id
		Long countyId = new Long(0);
		if(prr !=null && prr.getParameter("countyId")!=null){
			countyId = new Long(prr.getParameter("countyId"));
		}else if(request.getParameter("countyId")!=null){
			countyId = new Long(request.getParameter("countyId"));
		}
		log.debug("CountyId=" + countyId);
		if(countyId.equals(new Long(0))){
			throw new ApplicationException("CountyId Id is needed to edit the Geographic Area County");
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
//		get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(facilityCountyForm.getFacilityId()!=null){
			facilityId = facilityCountyForm.getFacilityId();
		}
		log.debug("facilityId----"+facilityId);
		
		if(facilityService.isFeedBack(facilityId, user)){
			facilityAddressService.softDeleteGeographicAreaCounty(facilityId,geographicAreaId, countyId, user);
			return mapping.findForward("success");
		}
		
		GeographicAreaCounty gac = facilityAddressService.getGeographicAreaCounty(geographicAreaId, countyId);
		if(gac!=null){
			if(gac.getPrimaryFlag()=='Y'){
				//check if coordinate type is C then delete Absolute loaction point
				AbsoluteLocationPoint alp= facilityAddressService.getFacilityCoordinates(facilityId);
				if(alp!=null){
					if(alp.getCoordinateTypeCode()!=null &&  alp.getCoordinateTypeCode().charValue()=='C'){
						facilityAddressService.deleteFacilityCoordinates(alp);
					}
				}
			}
			facilityAddressService.deleteGeographicAreaCounty(facilityId,geographicAreaId, countyId, user);
			//check if primary county is being deleted and the coordinate type is set to c then delete the Absolute location point	
		}		
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
