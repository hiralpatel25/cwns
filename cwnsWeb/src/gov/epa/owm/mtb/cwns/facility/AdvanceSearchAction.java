package gov.epa.owm.mtb.cwns.facility;

import java.util.Collection;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;

import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;

import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.DocumentService;



public class AdvanceSearchAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userrole = user.getCurrentRole();
		
		Collection reviewStatusList = facilityService.getReviewStatusRefs();
		req.setAttribute("reviewStatusList", reviewStatusList);
		
		Collection overAllNatureTypes = facilityService.getOverAllNatureTypes();
		req.setAttribute("overAllNatureTypes", overAllNatureTypes);		

		Collection states = facilityService.getStates();
		req.setAttribute("states", states);		
		req.setAttribute("defaultLocation", userrole.getLocationId());
		req.setAttribute("userType", userrole.getLocationTypeId());
		if(UserService.LOCATION_TYPE_ID_REGIONAL.equalsIgnoreCase(userrole.getLocationTypeId()))
			req.setAttribute("statesInRegion", facilityService.getStateIdsForRegion(userrole.getLocationId()));
		
		
		req.setAttribute("populationRefs", populationService.getPopulationRefs("11"));
		req.setAttribute("flowRefs", flowService.getFlowRefs());
		req.setAttribute("locationRefs", userService.getLocationRefs());
		req.setAttribute("documentTypeRefs", documentService.getDocumentTypeRefs());
		
			String key ="help."+user.getCurrentRole().getLocationTypeId()+".advancedsearch";
		    String defaultkey = "help.advancedsearch"; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		req.setAttribute("helpKey", helpKey);
		
		return mapping.findForward("success");
	}
	
	/* set the facility service */
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }
    
    /* set the User service */
    private static UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }
    
    /* set the populationService */
    private static PopulationService populationService;
	public void setPopulationService(PopulationService populationService) {
		AdvanceSearchAction.populationService = populationService;
	}
    
	/* set the FlowService */
    private static FlowService flowService;
	public void setFlowService(FlowService flowService) {
		AdvanceSearchAction.flowService = flowService;
	}
	

	/* set the DocumentService */
    private static DocumentService documentService;
	public void setDocumentService(DocumentService documentService) {
		AdvanceSearchAction.documentService = documentService;
	}
	
}
