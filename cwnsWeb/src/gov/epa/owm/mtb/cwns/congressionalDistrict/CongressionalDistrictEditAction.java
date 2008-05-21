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
import gov.epa.owm.mtb.cwns.model.GeographicAreaCongDistrict;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;

public class CongressionalDistrictEditAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		CongressionalDistrictForm conDistrictForm = (CongressionalDistrictForm)form;
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);

		//get the conDistrict Id
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
		
		GeographicAreaCongDistrict gacd= facilityAddressService.getGACDByPrimaryKey(conDistrictId, geographicAreaId);
		
		// set the form varaibles
		conDistrictForm.setGeographicAreaId(gacd.getId().getGeographicAreaId());
		conDistrictForm.setConDistrictId(gacd.getId().getCongressionalDistrictId());
		conDistrictForm.setPrimary(gacd.getPrimaryFlag());
		String lastTwoDigits = gacd.getId().getCongressionalDistrictId().substring(2);
		if("01".equals(lastTwoDigits))
			conDistrictForm.setConDistrictName(lastTwoDigits.substring(1)+"st Congressional District");
		else if ("02".equals(lastTwoDigits))
			conDistrictForm.setConDistrictName(lastTwoDigits.substring(1)+"nd Congressional District");
		else if("03".equals(lastTwoDigits))
			conDistrictForm.setConDistrictName(lastTwoDigits.substring(1)+"rd Congressional District");
		else conDistrictForm.setConDistrictName(lastTwoDigits.charAt(0)=='0'?lastTwoDigits.substring(1)+"th Congressional District"
				                  :lastTwoDigits+"th Congressional District");
		conDistrictForm.setMode("edit");
		
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
