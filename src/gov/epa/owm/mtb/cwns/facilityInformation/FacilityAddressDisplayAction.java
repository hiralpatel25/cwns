package gov.epa.owm.mtb.cwns.facilityInformation;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityAddress;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;

public class FacilityAddressDisplayAction extends CWNSAction {

	private Logger log = Logger.getLogger(this.getClass());

    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    	
    	PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    	
    	FacilityAddressForm facilityAddressForm = (FacilityAddressForm)form;
    	
        //	get the facility ID from the page parameter - configured as input parameter in provider.xml
		Long facilityId = new Long(0);
		if(prr.getParameter("facilityId")!=null) facilityId = new Long(prr.getParameter("facilityId"));
        facilityAddressForm.setFacilityId(facilityId);
         
        if (facilityAddressService.isNonPointSource(facilityId)){
        	return null;
        }
            	
    	String Sreviewstatustype ="";
    	String Freviewstatustype ="";
			
        //	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER); 
		        
        // Get Survey Facility Object
        Facility f = facilityService.findByFacilityId(facilityId.toString());
        
        // Get feedback copy 
		Facility feedbackVersion = (Facility)facilityService.getFeedbackVersionOfFacility(facilityId.toString());	
		 
		if (f != null && f.getReviewStatusRef() != null)
		   Sreviewstatustype = f.getReviewStatusRef().getReviewStatusId();
		if (feedbackVersion != null && feedbackVersion.getReviewStatusRef() != null)
		   Freviewstatustype = feedbackVersion.getReviewStatusRef().getReviewStatusId();
		if ((ReviewStatusRefService.STATE_ASSIGNED.equalsIgnoreCase(Sreviewstatustype) || ReviewStatusRefService.STATE_IN_PROGRESS.equalsIgnoreCase(Sreviewstatustype) || 
				ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equalsIgnoreCase(Sreviewstatustype)) && (feedbackVersion != null &&
			   (ReviewStatusRefService.LOCAL_ASSIGNED.equalsIgnoreCase(Freviewstatustype) || ReviewStatusRefService.LOCAL_IN_PROGRESS.equalsIgnoreCase(Freviewstatustype)))) {
			facilityAddressForm.setShowWarningMessage("Y");	
		}
		if (f!=null)
		  facilityAddressForm.setState(f.getLocationId());
		Collection states = facilityAddressService.getStates();
    	FacilityAddress facAddress = facilityAddressService.getFacilityAddress(facilityId);
    	if (facAddress != null){
		facilityAddressForm.setAddress1(facAddress.getStreetAddress1());
		facilityAddressForm.setAddress2(facAddress.getStreetAddress2());
		facilityAddressForm.setCity(facAddress.getCity());
		if (facAddress.getStateId()!=null)
		  facilityAddressForm.setState(facAddress.getStateId());
		facilityAddressForm.setZipCode(facAddress.getZip()==null?"":facAddress.getZip().trim());
		facilityAddressForm.setIsSourcedFromNPDES(facAddress.getSourcedFromNpdesFlag());
		if (facilityService.isUpdatable(user, facilityId) && 
				"N".equalsIgnoreCase((new Character(facAddress.getSourcedFromNpdesFlag())).toString())){
		    facilityAddressForm.setIsUpdatable('Y');
		}
    	}else 
    		if (facilityService.isUpdatable(user, facilityId))
    			facilityAddressForm.setIsUpdatable('Y');
		
    	if (states != null)
			facilityAddressForm.setStates(states);
		request.setAttribute("facilityAddress", facilityAddressForm);
		request.setAttribute("required", facilityAddressService.areFieldsRequired(facilityId)?"true":"false");
		//return mapping.findForward("success");
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
