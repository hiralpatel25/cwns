package gov.epa.owm.mtb.cwns.watershed;

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
import gov.epa.owm.mtb.cwns.congressionalDistrict.CongressionalDistrictForm;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCongDistrict;
import gov.epa.owm.mtb.cwns.model.GeographicAreaWatershed;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;

public class WatershedEditAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		WatershedForm watershedForm = (WatershedForm)form;
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);

		//get the watershed Id
		String watershedId=null;
		if(prr !=null && prr.getParameter("watershedId")!=null){
			watershedId = prr.getParameter("watershedId");
		}else if(request.getParameter("watershedId")!=null){
			watershedId = request.getParameter("watershedId");
		}
		log.debug("watershedId=" + watershedId);
		if(watershedId == null){
			throw new ApplicationException("Congressional District Id is needed to edit the Geographic Area Congressional District");
		}
		
		//get the geographicArea Id
		Long geographicAreaId = new Long(0);
		if(prr!=null && prr.getParameter("geographicAreaId")!=null) {
			geographicAreaId = new Long(prr.getParameter("geographicAreaId"));
		}else if(request.getParameter("geographicAreaId")!=null){
			geographicAreaId = new Long(request.getParameter("geographicAreaId"));
		}
		log.debug("GeographicAreaId=" + geographicAreaId);
		if(geographicAreaId == new Long(0)){
			throw new ApplicationException("geographicArea Id is needed to edit the Geographic Area Congressional District");
		}
		
		GeographicAreaWatershed gaw= facilityAddressService.getGAWatershedByPrimaryKey(watershedId, geographicAreaId);
		
		// set the form varaibles
		watershedForm.setGeographicAreaId(gaw.getId().getGeographicAreaId());
		watershedForm.setWatershedId(gaw.getId().getWatershedId());
		watershedForm.setPrimary(gaw.getPrimaryFlag());
		watershedForm.setWatershedName(gaw.getWatershedRef().getName());
		watershedForm.setMode("edit");
		
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
