package gov.epa.owm.mtb.cwns.manageCostCurve;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.service.ManageCostCurveService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.UserService;

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

public class ManageCostCurveAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	ManageCostCurveForm manageCostCurveForm = (ManageCostCurveForm) form;
    	
		log.debug(" IN - manageCostCurveForm--> " + manageCostCurveForm.toString());

        //Get user privilages
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userRole = user.getCurrentRole();
		
		//this should be commented out during deployment
		//userRole.setType(UserRole.LOCAL);
		
		String userType = userRole.getLocationTypeId();

		if(UserService.LOCATION_TYPE_ID_LOCAL.equals(userType))		
			return null;
		
	    PortletRenderRequest prr = (PortletRenderRequest)
		      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		
		    /* determine what needs to be done */
		String action = (manageCostCurveForm.getManageCostCurveAct() != null) ? 
				manageCostCurveForm.getManageCostCurveAct() : "None";

		log.debug("Action: " + action);
        
		//This facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null)
			facId = Long.parseLong(req.getParameter("facilityId"));

		log.debug("req.getParameter(facilityId): " + facId);

		// if facId is available in the form, use it instead
		if(manageCostCurveForm.getManageCostCurveFacilityId() != ManageCostCurveForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form instead
			facId = manageCostCurveForm.getManageCostCurveFacilityId();			
		}
		
		manageCostCurveForm.setManageCostCurveFacilityId(facId);

		ActionForward actionFwd = mapping.findForward("success");

		String docId = "";
		
		if(manageCostCurveForm.getDocumentId()<=0)
			docId = (String)httpSess.getAttribute(facId + "_ActiveDocId");
		else
			docId = new Long(manageCostCurveForm.getDocumentId()).toString();
		
		log.debug("docId: " + docId);
		
		if(docId != null && !docId.equals(""))
		{
			manageCostCurveForm.setDocumentId(new Long(docId).longValue());
		}
	
		if (action.equals("save"))
		{	
			if(isTokenValid(req, true))
			{
				log.debug("save cost for docId: " + docId);				
				manageCostCurveService.saveOrUpdateCostCurve(manageCostCurveForm, user);				
			   resetToken(req);
			}		
		}
		
		saveToken(req);			

		ArrayList facilityCostCurveList = new ArrayList();
		ArrayList assignedCostCurveList = new ArrayList();
		
		if(docId != null && !docId.equals(""))
		{
			facilityCostCurveList = (ArrayList)manageCostCurveService.getCostCurveList(facId, Long.parseLong(docId));
			assignedCostCurveList = (ArrayList)manageCostCurveService.getAssignedCostCurveList(facilityCostCurveList);
			
			manageCostCurveForm.setAtLeastOneEnabled(manageCostCurveService.atLeastOneEnabled(facilityCostCurveList));
		
			log.debug("facilityCostCurveList: " + (facilityCostCurveList==null?0:facilityCostCurveList.size()));
		}

		manageCostCurveForm.setAssignedFacilityCostCurveIds((String[])assignedCostCurveList.toArray(new String[0]));
		manageCostCurveForm.setUpdatedFacilityCostCurveIds((String[])assignedCostCurveList.toArray(new String[0]));
				
        // Check if facility is updatable or not and set form attribute
 	     if (facilityService.isUpdatable(user, new Long(facId))){
 	    	manageCostCurveForm.setIsUpdatable("Y");
 		  }
 	     else
 	     {
 	    	manageCostCurveForm.setIsUpdatable("N");
 	     }

 	    req.setAttribute("manageCostCurveList", facilityCostCurveList);
		req.setAttribute("manageCostCurveForm", manageCostCurveForm);
	
		return actionFwd;
	}

	private ManageCostCurveService manageCostCurveService;

	public void setManageCostCurveService(ManageCostCurveService ss) {
		manageCostCurveService = ss;
	}
    private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
        
}
