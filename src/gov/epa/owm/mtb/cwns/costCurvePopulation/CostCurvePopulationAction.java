package gov.epa.owm.mtb.cwns.costCurvePopulation;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.service.CostCurvePopulationService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.impl.CostCurvePopulationServiceImpl;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CostCurvePopulationAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	CostCurvePopulationForm costCurvePopulationForm = (CostCurvePopulationForm) form;
    	
		log.debug(" IN - costCurvePopulationForm--> " + costCurvePopulationForm.toString());

        //Get user privilages
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userRole = user.getCurrentRole();
		
		//this should be commented out during deployment
		//userRole.setType(UserRole.LOCAL);
		// do not show the portlet for local users
		if (UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(userRole.getLocationTypeId())){
        	return null;
        }
		String userType = userRole.getLocationTypeId();

	    PortletRenderRequest prr = (PortletRenderRequest)
		      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		
		    /* determine what needs to be done */
		String action = (costCurvePopulationForm.getCostCurvePopulationAct() != null) ? 
				costCurvePopulationForm.getCostCurvePopulationAct() : "None";

		log.debug("Action: " + action);
        
		//This facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null)
			facId = Long.parseLong(req.getParameter("facilityId"));

		log.debug("req.getParameter(facilityId): " + facId);

		// if facId is available in the form, use it instead
		if(costCurvePopulationForm.getCostCurvePopulationFacilityId() != CostCurvePopulationForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form instead
			facId = costCurvePopulationForm.getCostCurvePopulationFacilityId();			
		}
		
		costCurvePopulationForm.setCostCurvePopulationFacilityId(facId);

		ActionForward actionFwd = mapping.findForward("success");
	    
		ArrayList errorsList = new ArrayList();
		ArrayList warningsList = new ArrayList();
		boolean saveHappened = false;
		
		if (action.equals("save"))
		{	
			costCurvePopulationService.validateFormValues(costCurvePopulationForm, errorsList, warningsList);
			
			if(errorsList.size() == 0)
			{
				if(isTokenValid(req, true))
				{			
					costCurvePopulationService.saveOrUpdateCostCurvePopulation(costCurvePopulationForm, 
			                user);
					saveHappened = true;
				   resetToken(req);
				}	
			}
		}
		
		saveToken(req);			
		
        // Check if facility is updatable or not and set form attribute
 	     if (facilityService.isUpdatable(user, new Long(facId))){
 	    	costCurvePopulationForm.setIsUpdatable("Y");
 		  }
 	     else
 	     {
 	    	costCurvePopulationForm.setIsUpdatable("N");
 	     }
 	     
 	     if(errorsList.size()>0 || warningsList.size()>0)
 	     {
 	    	costCurvePopulationForm.setShowMessageZone("Y");
 	     }
 	     else
 	    	costCurvePopulationForm.setShowMessageZone("N");
 	     
 	   costCurvePopulationForm.setPopulationPerHouseResidential(costCurvePopulationService.getPopulationPerHouse(facId, CostCurvePopulationServiceImpl.RESIDENTIAL));
 	   costCurvePopulationForm.setPopulationPerHouseNonResidential(costCurvePopulationService.getPopulationPerHouse(facId, CostCurvePopulationServiceImpl.NONRESIDENTIAL));
 	   costCurvePopulationForm.setPopulationPerHouseDisplayOnly(costCurvePopulationService.getPopulationPerHouseDisplayOnly(facId));

 	   if(errorsList.size() == 0)
 		 costCurvePopulationService.setDisplayObjectsInRequest(req, facId, costCurvePopulationForm, false);
 	   else
 		  costCurvePopulationService.setDisplayObjectsInRequest(req, facId, costCurvePopulationForm, true);  
 	  
 	   float resClusteredPopulationPerHouse = populationService.getResidentialPopulationPerHouse(new Long(facId));
	   float nonResClusteredPopulationPerHouse = populationService.getNonResidentialPopulationPerHouse(new Long(facId));
	   float resOWTSPopulationPerHouse = populationService.getResidentialOWTSPopulationPerHouse(new Long(facId));
	   float nonResOWTSPopulationPerHouse = populationService.getNonResidentialOWTSPopulationPerHouse(new Long(facId));
	   costCurvePopulationForm.setResClusteredPopulationPerHouse(resClusteredPopulationPerHouse);
	   costCurvePopulationForm.setNonResClusteredPopulationPerHouse(nonResClusteredPopulationPerHouse);
	   costCurvePopulationForm.setResOWTSPopulationPerHouse(resOWTSPopulationPerHouse);
	   costCurvePopulationForm.setNonResOWTSPopulationPerHouse(nonResOWTSPopulationPerHouse);
 	   
		req.setAttribute("costCurvePopulationForm", costCurvePopulationForm);
		req.setAttribute("errorsList", errorsList);
		req.setAttribute("warningsList", warningsList);

		return actionFwd;
	}

	private CostCurvePopulationService costCurvePopulationService;

	public void setCostCurvePopulationService(CostCurvePopulationService ss) {
		costCurvePopulationService = ss;
	}
    private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }
    private PopulationService populationService;
 	
	 //  set the population service
	 public void setPopulationService(PopulationService ps){
	     populationService = ps;    	
	 }
}
