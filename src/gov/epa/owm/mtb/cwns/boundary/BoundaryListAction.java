package gov.epa.owm.mtb.cwns.boundary;

import java.util.Collection;

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
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class BoundaryListAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
        //	get the location ID from the page parameter - configured as input parameter in provider.xml
		String locationId = "";
		if(prr.getParameter("locationId")!=null) locationId = prr.getParameter("locationId");
		Collection boundaryList = facilityAddressService.getBoundaryListByLocation(locationId);
		request.setAttribute("boundaries", boundaryList);
		return mapping.findForward("success");
	}
	
	 //  set the facility address service
	  private FacilityAddressService facilityAddressService;
	  
	  public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		 this.facilityAddressService = facilityAddressService;
	  }    

}
