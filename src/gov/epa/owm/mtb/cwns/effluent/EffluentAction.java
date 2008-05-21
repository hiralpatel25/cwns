package gov.epa.owm.mtb.cwns.effluent;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityAdvancedTreatment;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.EffluentService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
public class EffluentAction extends CWNSAction {
	
	public static final String ACTION_DEFAULT      = "default_effluent";
	public static final String ACTION_PROCESS_EDIT = "process_edit_effluent";
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

   		PortletRenderRequest prr = (PortletRenderRequest)
   				request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
   		DynaActionForm eform = (DynaActionForm) form;
        ActionErrors errors = new ActionErrors();

	    //TODO: Check if provider session works in the portal environment
   		//ProviderSession pSession = prr.getSession();
	    HttpSession pSession = request.getSession(false);
		//ProviderSession pSession = prr.getSession();	    
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }
		String facilityIdString = prr.getParameter("facilityId");
		Long facilityId = new Long(facilityIdString);
  		CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);
  		  		
  		Collection advanceTreatmentTypes = effluentService.getAdvanceTreatmentTypes();
  		Collection effluentTreatmentLevels = effluentService.getEffluentTreatmentLevel();
  		request.setAttribute("advanceTreatmentTypes", advanceTreatmentTypes);
  		request.setAttribute("effluentTreatmentLevels", effluentTreatmentLevels);
  		
  		//get the present/projected flag
  		boolean present = facilityTypeService.isFacilityTypeStatusPresentAndApplicableToDataArea(facilityId, FacilityService.DATA_AREA_EFFLUENT);
  		boolean projected = facilityTypeService.isFacilityTypeStatusProjectedAndApplicableToDataArea(facilityId, FacilityService.DATA_AREA_EFFLUENT);
  		boolean updatable = facilityService.isUpdatable(currentUser, facilityId);
  		request.setAttribute("isUpdatable", ((updatable)?"Y":"N"));
  		request.setAttribute("present", ((present&&updatable)?"Y":"N"));
  		request.setAttribute("projected", ((projected&&updatable)?"Y":"N"));
  		
  		//get the present/projected disinfection flag
  		if (effluentService.isPresentdFacilityDisinfected(facilityId)){
  			eform.set("presentDisinfection", new Boolean(true));
  		}else{
  			eform.set("presentDisinfection", new Boolean(false));  			
  		}
  		
  		if (effluentService.isProjectedFacilityDisinfected(facilityId)){
  			eform.set("projectedDisinfection", new Boolean(true));  			
  		}else{
  			eform.set("projectedDisinfection", new Boolean(false));
  		}
  		
  		
  		//get the effluent treatment level 
  		long presentEffluentLevel =  effluentService.getPresentFacilityEffluentLevel(facilityId);
  		long projectedEffluentLevel =  effluentService.getProjectedFacilityEffluentLevel(facilityId);
  		
  		eform.set("presentEffluentLevelId", presentEffluentLevel+"");
  		eform.set("projectedEffluentLevelId", projectedEffluentLevel+"");
  		
  		boolean isIndicators = false; 
  		if(presentEffluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
  			Collection presentIndicators = effluentService.getPresentAdvanceTreatmentTypes(facilityId);
  			if(presentIndicators.size()>0){
  				eform.set("presentAdvanceTreatmentIndicators", getEffluentIds(presentIndicators));	
  			}
  			isIndicators = true;
  		}else{
  			eform.set("presentAdvanceTreatmentIndicators", new String[0]);
  		}
		request.setAttribute("presentInd", ((present&&updatable&&isIndicators)?"Y":"N"));
  		
		isIndicators = false;
  		if(projectedEffluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
  			Collection projectedIndicators = effluentService.getProjectedAdvanceTreatmentTypes(facilityId);
  			if(projectedIndicators.size()>0){
  			   eform.set("projectedAdvanceTreatmentIndicators", getEffluentIds(projectedIndicators));
  			}
  			isIndicators = true;
  		}else{
  			eform.set("projectedAdvanceTreatmentIndicators", new String[0]);
  		}
		request.setAttribute("projectedInd", ((projected&&updatable&&isIndicators)?"Y":"N"));
		
		request.setAttribute("presentDischargeSurfaceWater", ((dischargeService.isPresentDischargeToSurfaceWaters(facilityId))?"Y":"N"));
		request.setAttribute("projectedDischargeSurfaceWater", ((dischargeService.isProjectedDischargeToSurfaceWaters(facilityId))?"Y":"N"));
		
		//introduce warning/error messages to state users
		if (isStateView(currentUser)){
			Collection stateViewWarnings = getWarningMessagesForStateUser(facilityIdString);
			if (stateViewWarnings!=null && stateViewWarnings.size()>0){
				request.setAttribute("stateViewWarnings", stateViewWarnings);
			}
		}		
		return mapping.findForward("success");
	}	
	
	public String[] getEffluentIds(Collection advanceTreatmentTypes){
		String[] s = new String[advanceTreatmentTypes.size()];
		int i=0;
		for (Iterator iter = advanceTreatmentTypes.iterator(); iter.hasNext();) {
			FacilityAdvancedTreatment fat = (FacilityAdvancedTreatment) iter.next();			
			s[i]=fat.getId().getAdvancedTreatmentTypeId()+"";
			i++;
		}
		return s;		
	}
	
	//get warning messages for the state user
	private Collection getWarningMessagesForStateUser(String facilityIdString){
		//case 1: Warn user if current status is State Assigned, State In Progress 
		//or State Correction Requested, and a Feedback copy exist with status of 
		//Local Assigned or Local in Progress
		ArrayList stateViewWarnings = new ArrayList();
		String s_facilityReviewStatusId = "";
		Entity s_FacilityReviewStatus = reviewStatusRefService.findFacilityReviewStatus(facilityIdString);
		s_facilityReviewStatusId = s_FacilityReviewStatus.getKey();
	
		if (s_facilityReviewStatusId.equals(ReviewStatusRefService.STATE_ASSIGNED) ||
				s_facilityReviewStatusId.equals(ReviewStatusRefService.STATE_IN_PROGRESS) ||
				s_facilityReviewStatusId.equals(ReviewStatusRefService.STATE_CORRECTION_REQUESTED)){
			String s_cwnsNbr = reviewStatusRefService.getCWNSNbrByFacilityId(facilityIdString);
			
			Facility f_facility = facilityService.getFacilityByCwnsNbr(s_cwnsNbr);
			String f_facilityIdString = (new Long(f_facility.getFacilityId())).toString();
			if (!facilityIdString.equals(f_facilityIdString)){
				Entity f_FacilityReviewStatus = reviewStatusRefService.findFacilityReviewStatus(f_facilityIdString);
				String f_facilityReviewStatusId = f_FacilityReviewStatus.getKey();
				if (f_facilityReviewStatusId.equals(ReviewStatusRefService.LOCAL_ASSIGNED) ||
					s_facilityReviewStatusId.equals(ReviewStatusRefService.LOCAL_IN_PROGRESS)){
					stateViewWarnings.add("warn.effluent.feedback.warning");
				}
			}
		}
		
		//case 2: If present Effluent is Raw Discharge/Primary/Advanced Primary and a present Discharge 
		//method of Ocean Discharge, Outfall to Surface Waters, or Overland Flow with Discharge exist.
		Long facilityId = new Long(facilityIdString);
		long presentEffluentLevel = effluentService.getPresentFacilityEffluentLevel(facilityId);
		if ((presentEffluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_RAW_DISCHARGE_ID ||
				presentEffluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_PRIMARY_ID ||
				presentEffluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_ADAVANCE_PRIMARY_ID) &&
			dischargeService.isPresentDischargeToSurfaceWaters(facilityId)){
			stateViewWarnings.add("warn.effluent.presentDischarge");
		}
		
		//case 3: If projected Effluent is Raw Discharge/Primary/Advanced Primary and a projected Discharge 
		//method of Ocean Discharge, Outfall to Surface Waters, or Overland Flow with Discharge exist.
		long projectedEffluentLevel = effluentService.getProjectedFacilityEffluentLevel(facilityId);
		if ((projectedEffluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_RAW_DISCHARGE_ID ||
				projectedEffluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_PRIMARY_ID ||
				projectedEffluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_ADAVANCE_PRIMARY_ID) &&
			dischargeService.isProjectedDischargeToSurfaceWaters(facilityId)){
			stateViewWarnings.add("warn.effluent.projectedDischarge");
		}		
		return stateViewWarnings;
	}	
	
	//Helper method
	private boolean isStateView(CurrentUser currentUser){
		boolean isStateView = false; 
		isStateView = currentUser.getCurrentRole().getLocationTypeId().equals(UserService.LOCATION_TYPE_ID_LOCAL)?false:true;
		return isStateView;
	}	

	/**
	 * Perform business rule validation. 
	 * @param pocf
	 * @return
	 */
	private ActionErrors validateBusinessRules(EffluentForm eForm) {

		ActionErrors errors = new ActionErrors();
        
        return errors;
	}
    
    private EffluentService effluentService;
	public void setEffluentService(EffluentService effluentService) {
		this.effluentService = effluentService;
	}
	
	private FacilityTypeService  facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService facilityTypeService) {
		this.facilityTypeService = facilityTypeService;
	}	  		

	private FacilityService  facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	
	private DischargeService  dischargeService;
	public void setDischargeService(DischargeService dischargeService) {
		this.dischargeService = dischargeService;
	}
	
	private ReviewStatusRefService reviewStatusRefService;
	public void setReviewStatusRefService(
			ReviewStatusRefService reviewStatusRefService) {
		this.reviewStatusRefService = reviewStatusRefService;
	}       
}


