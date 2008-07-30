package gov.epa.owm.mtb.cwns.utilityManagement;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.UtilityManagementService;
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

public class UtilityManagementAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	UtilityManagementForm utilityManagementForm = (UtilityManagementForm) form;
    	
		log.debug(" IN - utilityManagementForm--> " + utilityManagementForm.toString());

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
		String action = (utilityManagementForm.getUtilityManagementAct() != null) ? 
				utilityManagementForm.getUtilityManagementAct() : "None";

		log.debug("Action: " + action);
        
		//This facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null)
			facId = Long.parseLong(req.getParameter("facilityId"));

		log.debug("req.getParameter(facilityId): " + facId);

		// if facId is available in the form, use it instead
		if(utilityManagementForm.getUtilityManagementFacilityId() != UtilityManagementForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form instead
			facId = utilityManagementForm.getUtilityManagementFacilityId();
		}
		
		//if the facilityId is for a feedback copy? then switch it to the survey copy.
		Facility facility = facilityService.findByFacilityId(String.valueOf(facId));
		if(facility!=null && facility.getVersionCode()==FacilityService.VERSION_FEEDBACK){
			Facility surveyFacility=facilityService.getFacilityByCwnsNbr(facility.getCwnsNbr());
			if(surveyFacility!=null){
				facId=surveyFacility.getFacilityId();				
			}else{
				log.error("Survey copy missing for facility Id" + facId);
			}
		}
		if(facId==0){
			log.error("Invalid facility ID=0");
			throw new ApplicationException("Invalid facility ID=0");
		}
		
		
		utilityManagementForm.setUtilityManagementFacilityId(facId);

		ActionForward actionFwd = mapping.findForward("success");

		if (action.equals("save"))
		{	
			if(isTokenValid(req, true))
			{		
				utilityManagementService.saveOrUpdateUtilityManagement(facId, utilityManagementForm.getResultListString(), user);			
			   resetToken(req);
			}		
		}

		saveToken(req);			
		
        // Check if facility is updatable or not and set form attribute
 	     if (facilityService.isUpdatable(user, new Long(facId), FacilityService.DATA_AREA_UTIL_MANAGEMENT)){
 	    	utilityManagementForm.setIsUpdatable("Y");
 		  }
 	     else
 	     {
 	    	utilityManagementForm.setIsUpdatable("N");
 	     }
 	    req.setAttribute("warnStateUser",utilityManagementService.warnStateUSerAboutLocal(facId, user));
 		req.setAttribute("utilityManagementList", utilityManagementService.getUtilityManagementList(facId));
 		req.setAttribute("utilityManagementStatusList", utilityManagementService.getUtilityManagementStatusList());
		req.setAttribute("utilityManagementForm", utilityManagementForm);

		return actionFwd;
	}

	private UtilityManagementService utilityManagementService;

	public void setUtilityManagementService(UtilityManagementService ss) {
		utilityManagementService = ss;
	}
    private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
}
