package gov.epa.owm.mtb.cwns.congressionalDistrict;

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
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;

public class CongressionalDistrictDisplayAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	    
		CongressionalDistrictForm conDistrictForm = (CongressionalDistrictForm)form;
  	    String Sreviewstatustype ="";
	    String Freviewstatustype ="";
        // 	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
        //	get the facility ID from the page parameter - configured as input parameter in provider.xml
		Long facilityId = new Long(0);
		if(prr.getParameter("facilityId")!=null) facilityId = new Long(prr.getParameter("facilityId"));
		
        //	Get Survey Facility Object
        Facility f = facilityService.findByFacilityId(facilityId.toString());
      
        //  Get feedback copy 
		Facility feedbackVersion = (Facility)facilityService.getFeedbackVersionOfFacility(facilityId.toString());	
		 
		if (f != null && f.getReviewStatusRef() != null)
		   Sreviewstatustype = f.getReviewStatusRef().getReviewStatusId();
		if (feedbackVersion != null && feedbackVersion.getReviewStatusRef() != null)
		   Freviewstatustype = feedbackVersion.getReviewStatusRef().getReviewStatusId();
		if ((ReviewStatusRefService.STATE_ASSIGNED.equalsIgnoreCase(Sreviewstatustype) || ReviewStatusRefService.STATE_IN_PROGRESS.equalsIgnoreCase(Sreviewstatustype) || 
				ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equalsIgnoreCase(Sreviewstatustype)) && (feedbackVersion != null &&
			   (ReviewStatusRefService.LOCAL_ASSIGNED.equalsIgnoreCase(Freviewstatustype) || ReviewStatusRefService.LOCAL_IN_PROGRESS.equalsIgnoreCase(Freviewstatustype)))) {
			conDistrictForm.setShowWarningMessage("Y");	
		}
		Collection conDistrictHelpers = facilityAddressService.getGeographicAreaConDistrictByFacilityId(facilityId);
		conDistrictForm.setConDistrictHelpers(conDistrictHelpers);
		conDistrictForm.setFacilityId(facilityId);
		if (f != null) conDistrictForm.setLocationId(f.getLocationId());
        // Check if facility id updatable or not and set form attribute
	       if (facilityService.isUpdatable(user, facilityId)){
	    	   conDistrictForm.setIsUpdatable("Y");
		   }
		
		String mode = (String)request.getAttribute("save_mode");
		if(mode!=null && !"errors".equals(mode)){
			conDistrictForm.setConDistrictId("");
			conDistrictForm.setMode("list");			
		}
				
        request.setAttribute("isFeedback",(facilityService.isFeedBack(facilityId, user)? "true" : "false"));
		
		request.setAttribute("conDistrictForm", conDistrictForm);
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
