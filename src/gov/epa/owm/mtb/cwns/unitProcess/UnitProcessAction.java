package gov.epa.owm.mtb.cwns.unitProcess;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.UnitProcessService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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

public class UnitProcessAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	UnitProcessForm unitProcessForm = (UnitProcessForm) form;
    	
		log.debug(" IN - unitProcessForm--> " + unitProcessForm.toString());

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
		String action = (unitProcessForm.getUnitProcessAct() != null) ? 
				unitProcessForm.getUnitProcessAct() : "None";

		log.debug("Action: " + action);
        
		//This facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null)
			facId = Long.parseLong(req.getParameter("facilityId"));

		log.debug("req.getParameter(facilityId): " + facId);

		// if facId is available in the form, use it instead
		if(unitProcessForm.getUnitProcessFacilityId() != UnitProcessForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form instead
			facId = unitProcessForm.getUnitProcessFacilityId();			
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
		
		unitProcessForm.setUnitProcessFacilityId(facId);

		ActionForward actionFwd = mapping.findForward("success");

		if (action.equals("save"))
		{	
			if(isTokenValid(req, true))
			{
				//log.debug("save cost for docId: " + docId);				
				unitProcessService.saveOrUpdateUnitProcess(unitProcessForm, user);				
			   resetToken(req);
			}		
			
			unitProcessForm.setIsAddAction("N");	
			unitProcessForm.setUnitProcessListId(-999);
		}
		else if(action.equals("edit"))
		{				
			unitProcessService.prepareFacilityUnitProcess(unitProcessForm);
			unitProcessForm.setDetailEditExpand("Y");
			unitProcessForm.setIsAddAction("N");		
		}
		else if(action.equals("delete"))
		{
			unitProcessService.deleteFacilityUnitProcess(unitProcessForm, user);
			
			unitProcessForm.initForm();
			unitProcessForm.setDetailEditExpand("N");			
			unitProcessForm.setIsAddAction("N");	
			unitProcessForm.setUnitProcessListId(-999);			
		}
		else if(action.equals("add"))
		{
			unitProcessForm.initForm();
			unitProcessForm.setDetailEditExpand("Y");
			unitProcessForm.setIsAddAction("Y");			
			unitProcessForm.setUnitProcessListId(-999);			
		}
		else if(action.equals("up"))
		{
			long radioId = unitProcessForm.getUnitProcessRadioId();
			
			unitProcessService.moveUnitProcess(unitProcessForm.getUnitProcessFacilityId(),
											   unitProcessForm.getTreatmentTypeId(),
											   unitProcessForm.getUnitProcessRadioId(), 1, user);
			unitProcessForm.initForm();
			unitProcessForm.setUnitProcessRadioId(radioId);
			unitProcessForm.setDetailEditExpand("N");			
			unitProcessForm.setIsAddAction("N");			
			unitProcessForm.setUnitProcessListId(-999);			
		}
		else if(action.equals("down"))
		{
			long radioId = unitProcessForm.getUnitProcessRadioId();

			unitProcessService.moveUnitProcess(unitProcessForm.getUnitProcessFacilityId(),
					   unitProcessForm.getTreatmentTypeId(),
					   unitProcessForm.getUnitProcessRadioId(), -1, user);
			
			unitProcessForm.initForm();
			unitProcessForm.setUnitProcessRadioId(radioId);
			unitProcessForm.setDetailEditExpand("N");			
			unitProcessForm.setIsAddAction("N");		
			unitProcessForm.setUnitProcessListId(-999);			
		}
		
		ArrayList unitProcessDropdownList = null;
		Collection unitProcessChangeTypeGlobalList = null;
		Collection availableUnitProcessChangeTypes = null;
		Collection selectedUnitProcessChangeTypes = null;
				
		if(action.equals("add") || action.equals("edit"))
		{
			unitProcessDropdownList = (ArrayList)unitProcessService.getUnitProcessDropdownList(facId, unitProcessForm.getTreatmentTypeId());

			unitProcessChangeTypeGlobalList = unitProcessService.getGlobalUnitProcessChangeTypes();
			
			if(action.equals("add"))
				selectedUnitProcessChangeTypes = new ArrayList();
			else
				selectedUnitProcessChangeTypes = unitProcessService.getFacilityUnitProcessChangeTypes(facId, 
																	 unitProcessForm.getTreatmentTypeId(), 
																	 unitProcessForm.getUnitProcessListId());
			
			availableUnitProcessChangeTypes = 
				unitProcessService.getAvailUnitProcessChangeTypes(unitProcessChangeTypeGlobalList, 
																  selectedUnitProcessChangeTypes,
																  unitProcessForm.getPresentProjectedFlag());
		}
		
		saveToken(req);			
		
		Collection unitProcessList = unitProcessService.getUnitProcessList(facId);			 
		 
        // Check if facility is updatable or not and set form attribute
 	     if (facilityService.isUpdatable(user, new Long(facId), FacilityService.DATA_AREA_UNIT_PROCESS)){
 	    	unitProcessForm.setIsUpdatable("Y");
 		  }
 	     else
 	     {
 	    	unitProcessForm.setIsUpdatable("N");
 	     }
 	     
		Calendar calendar = new GregorianCalendar();
		
		calendar.setTime(new Date());
		
		unitProcessForm.setCurrentYear((short)calendar.get(Calendar.YEAR));
		
		unitProcessForm.setWarnStateUser(unitProcessService.warnStateUSerAboutLocal(facId, user));
		 	     
		req.setAttribute("unitProcessChangeTypeGlobalList", unitProcessChangeTypeGlobalList);
		req.setAttribute("availableChangeTypes", availableUnitProcessChangeTypes);
		req.setAttribute("selectedChangeTypes", selectedUnitProcessChangeTypes);
		req.setAttribute("unitProcessDropdownList", unitProcessDropdownList);
		req.setAttribute("unitProcessForm", unitProcessForm);
		req.setAttribute("unitProcessGlobalList", unitProcessList);

		return actionFwd;
	}

	private UnitProcessService unitProcessService;

	public void setUnitProcessService(UnitProcessService ss) {
		unitProcessService = ss;
	}
    private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
}
