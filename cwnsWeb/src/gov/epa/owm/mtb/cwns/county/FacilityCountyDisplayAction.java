package gov.epa.owm.mtb.cwns.county;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FacilityCountyDisplayAction extends CWNSAction {

    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    	
    	PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
  	    
    	FacilityCountyForm facilityCountyForm = (FacilityCountyForm)form;

        // 	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
        //	get the facility ID from the page parameter - configured as input parameter in provider.xml
		Long facilityId = new Long(0);
		if(prr.getParameter("facilityId")!=null) facilityId = new Long(prr.getParameter("facilityId"));
		
        //	  Get Survey Facility Object
        Facility f = facilityService.findByFacilityId(facilityId.toString());		
        request.setAttribute("isFeedback",(f.getVersionCode()=='F'? "true" : "false"));
        
        //this flag is not longer required
		facilityCountyForm.setShowWarningMessage("N");
		
		Collection geographicAreaCounties = facilityAddressService.getGeographicAreaCountyByFacilityId(facilityId);
		facilityCountyForm.setGeographicAreaCounties(geographicAreaCounties);
		facilityCountyForm.setFacilityId(facilityId);
		if (f != null) facilityCountyForm.setLocationId(f.getLocationId());
         //	 Check if facility id updatable or not and set form attribute
	       if (facilityService.isUpdatable(user, facilityId)){
	    	   facilityCountyForm.setIsUpdatable("Y");
		   }
		
		String mode = (String)request.getAttribute("save_mode");
		if(mode!=null && !"errors".equals(mode)){			
			facilityCountyForm.setMode("list");	
			facilityCountyForm.setCountyId(0);
		}
		
		request.setAttribute("facilityCountyForm", facilityCountyForm);
		return mapping.findForward("success");
	}
    
    //  set the facility address service
    private FacilityAddressService facilityAddressService;
    
    //  set the facility service
    private FacilityService facilityService;
    
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  

	public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	}

}
