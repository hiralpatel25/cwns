package gov.epa.owm.mtb.cwns.facilityInformation;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FacilityInformationAction extends CWNSAction {

	private Logger log = Logger.getLogger(this.getClass());

    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
    	FacilityInformationForm facilityinfoform = (FacilityInformationForm)form;
	    PortletRenderRequest prr = (PortletRenderRequest)request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		    
        //Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		   	
		String facNum = facilityinfoform.getSurveyFacilityId();
		if (facNum == null){		
		   //get the facility ID from the page parameter - configured as input parameter in provider.xml
		   facNum = prr.getParameter("facilityId");
		}	
	       		    
		 log.debug("facilityId: " + facNum);
		 Long facilityId = new Long(facNum); 
		 /* obtain the Facility object */
		 Facility facility = new Facility();
		 String locationId = null;
		 if(facilityId.longValue()!=0){
		    	facility = facilityService.findByFacilityId(facNum);
		    	// Check if NPDES icon has to be shown or not and set the form attribute
		    	if (facilityService.isNpdesIconVisible(facilityId))
		    		facilityinfoform.setIsNpdesIconVisible("Y");
		    	
		    	// Get NPDES Facility Name and set the form attribute
		    	facilityinfoform.setNpdesFacilityName(facilityService.getNpdesFacilityName(facilityId));
		    	
				// Check if facility has Federal Needs
		    	if(!facility.getOwnerCode().equals(FacilityService.FACILITY_OWNER_PRIVATE)){
					if (facilityService.hasFederalNeeds(new Long(facNum)) && !facilityService.facilityTypesAllowsFederalNeeds(new Long(facNum))){
						facilityinfoform.setShowPrivate("N");
					}
		    	}
				
				// Check if facility id updatable or not and set form attribute
			       if (facilityService.isUpdatable(user, new Long(facNum))){
				       facilityinfoform.setIsUpdatable("Y");
				   }
		       // Check facility Overall type 
			       if (facilityService.isFacility(new Long(facNum))){
			    	   facilityinfoform.setIsFacility("Y");
			       }
			   // Check if user type is local and set form attribute 
			       if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
			    	   facilityinfoform.setIsLocalUser("Y"); 
			       }
				// Check if feedback version exists 
				Facility feedbackVersion = (Facility)facilityService.getFeedbackVersionOfFacility(facNum);	
				   		    
				String Sreviewstatustype = facility.getReviewStatusRef().getReviewStatusId();
				String Freviewstatustype = feedbackVersion == null?"":feedbackVersion.getReviewStatusRef().getReviewStatusId();
				if (("SAS".equalsIgnoreCase(Sreviewstatustype) || "SIP".equalsIgnoreCase(Sreviewstatustype) || "SCR".equalsIgnoreCase(Sreviewstatustype))
					&& (feedbackVersion != null &&
					   ("LAS".equalsIgnoreCase(Freviewstatustype) || "LIP".equalsIgnoreCase(Freviewstatustype)))) {
				    facilityinfoform.setShowWarningMessage("Y");	
				}
			    
				facilityinfoform.setSurveyFacilityId(new Long(facility.getFacilityId()).toString());
				facilityinfoform.setCwnsNbr(facility.getCwnsNbr()==null?"":facility.getCwnsNbr());
				facilityinfoform.setDescription(facility.getDescription()==null?"":facility.getDescription());
				facilityinfoform.setFacilityName(facility.getName()==null?"":facility.getName());
				facilityinfoform.setSystemName(facility.getSystem()==null?"":facility.getSystem().getName());
				facilityinfoform.setOwnerCode(facility.getOwnerCode());
				locationId= facility.getLocationId();
				facilityinfoform.setLocationId(locationId);
				facilityinfoform.setOwnerCodeValue("PUB".equalsIgnoreCase(facility.getOwnerCode())?"Public":"PRI".equalsIgnoreCase(facility.getOwnerCode())?
						                            "Private":"FED".equalsIgnoreCase(facility.getOwnerCode())?"Federal":"");
				facilityinfoform.setMilitaryFlag(facility.getMilitaryFlag());
				if (facility.getTmdlFlag()!=null){
				  facilityinfoform.setTmdlFlg(facility.getTmdlFlag().charValue()=='Y'?"Y":"N");
				}
				if (facility.getSourceWaterProtectionFlag()!=null){
				facilityinfoform.setSourceWaterProtectionFlg(facility.getSourceWaterProtectionFlag().charValue()=='Y'
					                                       ?"Y":"N");
				}

		 }else{
			 facilityinfoform.setIsUpdatable("Y");
			 facilityinfoform.setIsFacility("Y");
			 facilityinfoform.setShowWarningMessage("N");
			 facilityinfoform.setShowPrivate("Y");
			 locationId= user.getCurrentRole().getLocationId();
			 facilityinfoform.setLocationId(locationId);
			 facilityinfoform.setOwnerCode("PUB");
			 facilityinfoform.setOwnerCodeValue("Public");
			 facilityinfoform.setSurveyFacilityId("0");		 			 			 
		 }
		 //get stateId
		 StateRef s = facilityService.getStateByLocationId(locationId);
		 if(s!= null){
			 log.debug("State Id" + s.getStateId()); 
			 request.setAttribute("stateId", s.getStateId());
		 }else{
			 log.error("Error: unable to retrive state for locationId:" + locationId);
		 }
	     request.setAttribute("facilityinfo", facilityinfoform);	    		
         return mapping.findForward("success");
  }
   
   
	//set the facility service
    private FacilityService facilityService;
    
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
    

}
