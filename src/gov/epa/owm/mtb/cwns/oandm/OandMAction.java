package gov.epa.owm.mtb.cwns.oandm;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.model.OperationAndMaintenanceCost;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.OandMService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class OandMAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	OandMForm oandMForm = (OandMForm) form;
    	
		log.debug(" IN - oandMForm--> " + oandMForm.toString());

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
		String action = (oandMForm.getOandMAct() != null) ? 
				oandMForm.getOandMAct() : "None";

		log.debug("Action: " + action);
        
		//This facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null)
			facId = Long.parseLong(req.getParameter("facilityId"));

		log.debug("req.getParameter(facilityId): " + facId);

		// if facId is available in the form, use it instead
		if(oandMForm.getOandMFacilityId() != OandMForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form instead
			facId = oandMForm.getOandMFacilityId();			
		}
		
		oandMForm.setOandMFacilityId(facId);

		ActionForward actionFwd = mapping.findForward("success");
	    
		ArrayList oandMCategoryList = null;

		ArrayList oandMList = new ArrayList();
		
		if(facilityService.isFacility(new Long(facId)))
			oandMForm.setIsNPS("N");
		else
			oandMForm.setIsNPS("Y");

		Calendar calendar = new GregorianCalendar();
		
		calendar.setTime(new Date());
		
		oandMForm.setCurrentYear((short)calendar.get(Calendar.YEAR));
		
		if (action.equals("save"))
		{	
			if(isTokenValid(req, true))
			{
				log.debug("save o and M for facId: " + facId);				
				oandMService.saveOrUpdateOandM(oandMForm, 
		                user);				
			   resetToken(req);
			}		
			
			oandMForm.setIsAddAction("N");	
		}
		else if(action.equals("edit"))
		{
			OperationAndMaintenanceCost oandMEdit = oandMService.prepareOperationAndMaintenance(facId, oandMForm.getCategoryId(), oandMForm.getYear());
			
			if(oandMEdit!=null)
			{
				oandMForm.setCategoryId(oandMEdit.getOperAndMaintCategoryRef().getOperAndMaintCategoryId());
				oandMForm.setOldCategoryId(oandMForm.getCategoryId());
				oandMForm.setCategoryName(oandMEdit.getOperAndMaintCategoryRef().getName());
				oandMForm.setCollectionCost(oandMEdit.getCollectionOrMaintenanceCost()==null?0:oandMEdit.getCollectionOrMaintenanceCost().longValue());
				oandMForm.setPlantCost(oandMEdit.getPlantOrMonitoringCost()==null?0:oandMEdit.getPlantOrMonitoringCost().longValue());
				oandMForm.setYear(oandMEdit.getId().getCostYear());
				
				oandMForm.setDetailEditExpand("Y");
				oandMForm.setIsAddAction("N");	
			}					
		}
		else if(action.equals("delete") || action.equals("mark delete"))
		{
			oandMService.deleteOandM(oandMForm.getOandMFacilityId(), oandMForm.getCategoryId(), oandMForm.getYear(), user, true, false);
			oandMForm.initForm();
			oandMForm.setIsAddAction("N");	
		}
		else if(action.equals("new"))
		{
			oandMForm.initForm();
			oandMForm.setYear((short)calendar.get(Calendar.YEAR));
			oandMForm.setDetailEditExpand("Y");
			oandMForm.setIsAddAction("Y");			
		}

		if(action.equals("edit"))
			oandMCategoryList = (ArrayList)oandMService.getOandMCategoryList(true, oandMForm.getOandMFacilityId(), 
					                                                                oandMForm.getYear(), oandMForm.getCategoryId());
		else
			oandMCategoryList = (ArrayList)oandMService.getOandMCategoryList(false, oandMForm.getOandMFacilityId(), 0, 0);
		
		saveToken(req);			
		
		oandMList = (ArrayList)oandMService.getOandMList(facId);

        // Check if facility is updatable or not and set form attribute
 	     if (facilityService.isUpdatable(user, new Long(facId))){
 	    	oandMForm.setIsUpdatable("Y");
 		  }
 	     else
 	     {
 	    	oandMForm.setIsUpdatable("N");
 	     }
 	     
        // warn if feedback copy exists
     	req.setAttribute("feedbackCopyExists", "N");
     	if (facilityService.findByFacilityId(Long.toString(facId)).getVersionCode()=='S' && 
     			facilityService.getFeedbackVersionOfFacility(Long.toString(facId))!=null){
     		req.setAttribute("feedbackCopyExists", "Y");
     	}
 	     
 	    ArrayList oandMExcludedList = (ArrayList)oandMService.generateExcludedList(facId); 
 	    
 	    req.setAttribute("oandMExcludedList", oandMExcludedList);
 	    req.setAttribute("oandMCategoryList", oandMCategoryList);
 	    req.setAttribute("oandMList", oandMList);
		req.setAttribute("oandMForm", oandMForm);
		req.setAttribute("stateUser", user.isNonLocalUser()?"true":"false"); 
	
		return actionFwd;
	}

	private OandMService oandMService;

	public void setOandMService(OandMService ss) {
		oandMService = ss;
	}
    private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
}
