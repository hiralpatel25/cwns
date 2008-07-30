package gov.epa.owm.mtb.cwns.county;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;

public class FacilityCountyEditAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
     
		FacilityCountyForm facilityCountyForm = (FacilityCountyForm)form;
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);

		//get the facility Id
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
		
		//get the facilityType Id
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
		
		GeographicAreaCounty gac= facilityAddressService.getGeographicAreaCounty(geographicAreaId, countyId);
		
		// set the form varaibles
		facilityCountyForm.setGeographicAreaId(gac.getId().getGeographicAreaId());
		facilityCountyForm.setCountyId(gac.getId().getCountyId());
		facilityCountyForm.setCountyName(gac.getCountyRef().getName());
		facilityCountyForm.setFipsCode(gac.getCountyRef().getFipsCode());
		facilityCountyForm.setPrimary(gac.getPrimaryFlag());
		facilityCountyForm.setMode("edit");
		
        //	save token
		saveToken(request);
		
		return mapping.findForward("success");	
				
	}
	
    //  set the facility address service
    private FacilityAddressService facilityAddressService;
        
	public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	}

}
