package gov.epa.owm.mtb.cwns.facilityInformation;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class WebRITInformationAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Long facilityId = null;
		
		String facilityIdStr = request.getParameter("facilityId");
		String lat = request.getParameter("latitude");
		String lon = request.getParameter("longitude");
		String session = request.getParameter("session");
		
		//}
		log.debug("facilityId:" + facilityIdStr);
		log.debug("lat:" + lat);
		log.debug("lon:" + lon);
		log.debug("session"+session);
		
		if(facilityIdStr==null){
			log.error("WebRIT Information Action Error: Could not find facilityId");
			throw new ApplicationException("WebRIT Information Action Error: Could not find facilityId");
		}
		facilityId = new Long(facilityIdStr);
		AbsoluteLocationPoint alp= facilityAddressService.getFacilityCoordinates(facilityId);
		log.debug("facilityId:" + facilityId);
		//get Coordinates
		BigDecimal latitude =null;
		BigDecimal longitude =null;
		if(lat!=null && lon!=null && !"".equals(lat)&& !"".equals(lon)){
			latitude =new BigDecimal(lat);
			longitude =new BigDecimal(lon);
	    	log.debug("Lat passed in url: (" + latitude.toString() + ")  Long: (" + longitude.toString() + ")");
		}		
		String boundingBox =null;	
		if(latitude!=null && longitude!=null && latitude.floatValue()!=0.0 && longitude.floatValue()!=0.0 ){
			boundingBox = facilityAddressService.getBoundingBox(latitude, longitude);			
		}else{
			boundingBox = facilityAddressService.getBoundingBox(facilityId);
		}
	       
			
    	//get session
		String sessionId =null;
		if(session!=null && !"".equals(session)){
			sessionId =session;
		}else{
			//new session
			sessionId =facilityAddressService.getWebritSession();
		} 
    	String viewerUrl = CWNSProperties.getProperty("webrit.viewer.url");
    	String maxFeatures="0";
    	//if NPS CWNSNPS or OOFNHD
    	String viewerType =null;    	
		if (facilityService.isFacility(facilityId)|| (alp!=null && alp.getCoordinateTypeCode()!=null && alp.getCoordinateTypeCode().charValue()=='S')) {
			viewerType = CWNSProperties.getProperty("webrit.viewer.type.point",	"CWNSPS");
			maxFeatures = CWNSProperties.getProperty("webrit.viewer.type.point.maxFeatures", "1");
		} else {
			viewerType = CWNSProperties.getProperty("webrit.viewer.type.nps","CWNSNPS");
			maxFeatures = CWNSProperties.getProperty("webrit.viewer.type.nps.maxFeatures", "10");
			
		}
		String viewerProperties = CWNSProperties.getProperty("webrit.viewer.boundingbox.properties");    	
    	String registrationKey = CWNSProperties.getProperty("webrit.registration.key");
    	
    	//create xml    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("<webrit>");
    	sb.append("<viewerType>" + viewerType + "</viewerType>");
    	sb.append("<viewerUrl>" + viewerUrl + "</viewerUrl>");
    	sb.append("<sessionId>" + sessionId + "</sessionId>");
    	sb.append("<boundingBox>" + boundingBox + "</boundingBox>");
    	sb.append("<viewerProperties>" + viewerProperties + "</viewerProperties>");
    	sb.append("<regId>" + registrationKey + "</regId>");
    	sb.append("<maxFeatures>" + maxFeatures + "</maxFeatures>");
    	sb.append("</webrit>");
    	
    	//String url = viewerUrl+"?Type="+viewerType+"&SessionID="+sessionId+"&OffNHDLat="+ latitude.toString() +"&OffNHDLon="+  longitude.toString() +"&BBOX="+boundingBox+viewerProperties;
    	//log.debug("WebRIT Viewer Information" + XML);		
    	//get the web RIT url
    	//String url=facilityAddressService.getWebRITURL(facilityId);
    	//request.setAttribute("webritURL", url);
    	
		String xml =sb.toString();
		log.debug("WebRIT Viewer Information" + xml);
		
		request.setAttribute("xml", xml);
		return mapping.findForward("success");
	}
	
	FacilityAddressService facilityAddressService;
	public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	}
	
	FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}	

}
