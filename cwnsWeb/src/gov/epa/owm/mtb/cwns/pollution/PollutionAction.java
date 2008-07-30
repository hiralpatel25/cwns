package gov.epa.owm.mtb.cwns.pollution;


import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.service.PollutionService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PollutionAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	PollutionForm pollutionForm = (PollutionForm) form;
    	
		log.debug(" IN - pollutionForm--> " + pollutionForm.toString());

        //Get user privilages
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userRole = user.getCurrentRole();
		
		//this should be commented out during deployment
		//userRole.setType(UserRole.LOCAL);
		
		String userType = userRole.getLocationTypeId();

	    PortletRenderRequest prr = (PortletRenderRequest)
		      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		
		    /* determine what needs to be done */
		String action = (pollutionForm.getPollutionAct() != null) ? 
				pollutionForm.getPollutionAct() : "None";

		log.debug("Action: " + action);
        
		//This facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null)
			facId = Long.parseLong(req.getParameter("facilityId"));

		log.debug("req.getParameter(facilityId): " + facId);

		// if facId is available in the form, use it instead
		if(pollutionForm.getPollutionFacilityId() != PollutionForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form instead
			facId = pollutionForm.getPollutionFacilityId();			
		}
		
		pollutionForm.setPollutionFacilityId(facId);

		ActionForward actionFwd = mapping.findForward("success");
	    
		if(pollutionForm.getPollutionAct().equals("save"))
		{
			pollutionService.updatePollutionRecords((new Long(facId)).toString(), pollutionForm.getCommaDilimitedPollutionProblemIds(), user);
		}
		
		Collection pollutionCategoryGroupProblemList = null;
		Collection pollutionGroupList = null;
		Collection availablePollutionProblemTypes = null;
		Collection selectedPollutionProblemTypes = null;
		
		pollutionCategoryGroupProblemList = pollutionService.getPollutionGroupProblemInfo((new Long(facId)).toString());
		pollutionGroupList = pollutionService.getPollutionGroupInfo((new Long(facId)).toString());
		selectedPollutionProblemTypes = pollutionService.getFacilityPollutionProblems((new Long(facId)).toString());
		availablePollutionProblemTypes = pollutionService.getPollutionProblemInfo(pollutionCategoryGroupProblemList, selectedPollutionProblemTypes);
		
        // Check if facility is updatable or not and set form attribute
 	     if (facilityService.isUpdatable(user, new Long(facId))){
 	    	pollutionForm.setIsUpdatable("Y");
 		  }
 	     else
 	     {
 	    	pollutionForm.setIsUpdatable("N");
 	     }
 	     	    
 	    req.setAttribute("pollutionCategoryGroupProblemList", pollutionCategoryGroupProblemList);
 	    req.setAttribute("pollutionGroupList", pollutionGroupList);
 	    req.setAttribute("availablePollutionProblemTypes", availablePollutionProblemTypes);
 	    req.setAttribute("selectedPollutionProblemTypes", selectedPollutionProblemTypes);

		req.setAttribute("pollutionForm", pollutionForm);
	
		return actionFwd;
	}

	private PollutionService pollutionService;

	public void setPollutionService(PollutionService ss) {
		pollutionService = ss;
	}
    private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
}