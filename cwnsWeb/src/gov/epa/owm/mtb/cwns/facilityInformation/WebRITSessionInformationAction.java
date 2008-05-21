package gov.epa.owm.mtb.cwns.facilityInformation;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class WebRITSessionInformationAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//facilityID
		Long facilityId = null;
		String facilityIdStr = request.getParameter("facilityId");
		log.debug("facilityId:" + facilityIdStr);
		if(facilityIdStr==null){
			log.error("WebRIT Information Action Error: Could not find facilityId");
			throw new ApplicationException("WebRIT Information Action Error: Could not find facilityId");
		}
		facilityId = new Long(facilityIdStr);
		
		//session Id
		String sessionId = request.getParameter("sessionId");
		if(sessionId==null){
			log.error("WebRIT Information Action Error: Could not find sessionId");
			throw new ApplicationException("WebRIT Information Action Error: Could not find sessionId");
		}
		
		//session Id
		String updatable = request.getParameter("updatable");
		if(updatable==null){
			log.error("WebRIT Information Action Error: Could not find updatable");
			throw new ApplicationException("WebRIT Information Action Error: Could not find updatable");
		}
		
		if("Y".equals(updatable)){
		//get the session data
		  String xml =facilityAddressService.getWebRITSessionInformation(sessionId);
		  log.debug("WebRIT session lat and long" + xml);			
		  request.setAttribute("xml", xml);
		}
		
		return mapping.findForward("success");    	
	}
	
	FacilityAddressService facilityAddressService;
	public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	}	

}
