package gov.epa.owm.mtb.cwns.effluent;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.FacilityAdvancedTreatment;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.EffluentService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
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
public class EffluentSaveAction extends CWNSAction {
	
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
  		char presentDisinfectionFlag = ((Boolean)eform.get("presentDisinfection")).booleanValue()? 'Y':'N';
  		char projectedDisinfectionFlag = ((Boolean)eform.get("projectedDisinfection")).booleanValue()? 'Y':'N';  		
  		String presentEffluentLevelId = (String)eform.get("presentEffluentLevelId");
  		String projectedEffluentLevelId= (String)eform.get("projectedEffluentLevelId");
  		String[] presentAdvanceTreatmentIndicators = (String[])eform.get("presentAdvanceTreatmentIndicators");
  		String[] projectedAdvanceTreatmentIndicators = (String[])eform.get("projectedAdvanceTreatmentIndicators");

  		//setup cost curves for rerun
  		if (isStateView(currentUser)){
  			addCostCurvesForRerun(presentEffluentLevelId, projectedEffluentLevelId, presentAdvanceTreatmentIndicators, 
  					projectedAdvanceTreatmentIndicators, facilityIdString);
  		}
  		
  		//save facility effluent info
  		if(presentEffluentLevelId!=null && !"".equals(presentEffluentLevelId)){
  			effluentService.saveEffluent(facilityId, (new Long(presentEffluentLevelId)).longValue(), presentAdvanceTreatmentIndicators, 
  				EffluentService.EFFLUENT_LEVEL_TYPE_PRESENT, presentDisinfectionFlag, currentUser.getUserId());
  		}
  		
  		if(projectedEffluentLevelId!=null && !"".equals(projectedEffluentLevelId)){
  			effluentService.saveEffluent(facilityId, (new Long(projectedEffluentLevelId)).longValue(), projectedAdvanceTreatmentIndicators, 
  				EffluentService.EFFLUENT_LEVEL_TYPE_PROJECTED, projectedDisinfectionFlag, currentUser.getUserId());
  		}
  		
  		log.debug("FacilityId: " + facilityId);
  		log.debug("presentEffluentLevelId" + presentEffluentLevelId );
  		log.debug("projectedEffluentLevelId"+ projectedEffluentLevelId);
  		log.debug("presentAdvanceTreatmentIndicators"+ presentAdvanceTreatmentIndicators.length);
  		log.debug("projectedAdvanceTreatmentIndicators" + projectedAdvanceTreatmentIndicators.length);
  		facilityService.performPostSaveUpdates(facilityId, FacilityService.DATA_AREA_EFFLUENT, currentUser);
  		//save and delete
		return mapping.findForward("success");
	}	
	
	private void addCostCurvesForRerun(String presentEffluentLevelId, String projectedEffluentLevelId, 
			String[] presentAdvanceTreatmentIndicators, String[] projectedAdvanceTreatmentIndicators, 
			String facilityIdString){
		Long facilityId = new Long(facilityIdString);		
		
		// Present Advanced Treatment types
		boolean presentAdvTTChanged = false;
  		if(presentEffluentLevelId!=null && !"".equals(presentEffluentLevelId)){
  			//On Save: If present Effluent Level is changed, set the following Cost Curves up for Rerun, for this facility:
  			//Increase Treatment, Increase Treatment and Capacity
  	  		if(Long.parseLong(presentEffluentLevelId) != effluentService.getPresentFacilityEffluentLevel(facilityId)){
  	  			presentAdvTTChanged = true;
  	  		}  			
  		}  		
  		if (!presentAdvTTChanged){
  			//On Save: If at least 1 present Advanced Treatment is changed, set the following Cost Curves up for Rerun, for this facility:
  			//Increase Treatment, Increase Treatment and Capacity  		
  			Collection preAdvTT = effluentService.getPresentAdvanceTreatmentTypes(facilityId);  		   		
  			if ((preAdvTT!=null && presentAdvanceTreatmentIndicators==null) ||
  					(preAdvTT==null && presentAdvanceTreatmentIndicators!=null)){
  				presentAdvTTChanged = true;
  			}else if (preAdvTT!=null && presentAdvanceTreatmentIndicators!=null &&
  					preAdvTT.size() != presentAdvanceTreatmentIndicators.length){
  				presentAdvTTChanged = true;
  			}else{
  				//loop thru and compare the present treatment types
  				if (preAdvTT!=null && preAdvTT.size()>0){
  					Iterator preAdvTTIter = preAdvTT.iterator();
  					while(preAdvTTIter.hasNext()){
  						FacilityAdvancedTreatment fat = (FacilityAdvancedTreatment)preAdvTTIter.next();
  						String savedTTIndicator = Long.toString(fat.getAdvancedTreatmentTypeRef().getAdvancedTreatmentTypeId());
  						if (!isTTIndicatorRetained(savedTTIndicator, presentAdvanceTreatmentIndicators)){
  							presentAdvTTChanged = true;
  							break;
  						}
  					}
  				}
  			}
  		}

  		if (presentAdvTTChanged){
  			ArrayList advCostCurveIdList = new ArrayList();
  			advCostCurveIdList.add(CostCurveService.CODE_INCREASE_LEVEL_TREATMENT);
  			advCostCurveIdList.add(CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT);
  			dischargeService.setUpCostCurveCodesForRerun(advCostCurveIdList, facilityIdString);
  		}  		
  		
  		//Projected Advanced Treatment types
  		boolean futAdvTTChanged = false;
  		if(projectedEffluentLevelId!=null && !"".equals(projectedEffluentLevelId)){
  	  		//On Save: If projected Effluent Level is changed, set the following Cost Curves up for Rerun, for this facility:
  	  		//New Treatment Plant, Increase Capacity, Increase Treatment, Increase Treatment and Capacity, Replace Treatment Plant
  			if(Long.parseLong(projectedEffluentLevelId) != effluentService.getProjectedFacilityEffluentLevel(facilityId)){
  				futAdvTTChanged = true;
  			}  			
  		}  		
  		
  		if (!futAdvTTChanged){
  			//On Save: If at least 1 projected Advanced Treatment  is changed, set the following Cost Curves up for Rerun, for this facility:
  			//New Treatment Plant, Increase Capacity, Increase Treatment, Increase Treatment and Capacity, Replace Treatment Plant
  			Collection futAdvTT = effluentService.getProjectedAdvanceTreatmentTypes(facilityId);
  			if ((futAdvTT!=null && projectedAdvanceTreatmentIndicators==null) ||
  					(futAdvTT==null && projectedAdvanceTreatmentIndicators!=null)){
  				futAdvTTChanged = true;
  			}else if (futAdvTT!=null && projectedAdvanceTreatmentIndicators!=null &&
  					futAdvTT.size() != projectedAdvanceTreatmentIndicators.length){
  				futAdvTTChanged = true;
  			}else{
  				//loop thru and compare the db values
  				if (futAdvTT!=null && futAdvTT.size()>0){
  					Iterator futAdvTTIter = futAdvTT.iterator();
  					while(futAdvTTIter.hasNext()){
  						FacilityAdvancedTreatment fat = (FacilityAdvancedTreatment)futAdvTTIter.next();
  						String savedTTIndicator = Long.toString(fat.getAdvancedTreatmentTypeRef().getAdvancedTreatmentTypeId());
  						if (!isTTIndicatorRetained(savedTTIndicator, projectedAdvanceTreatmentIndicators)){
  							futAdvTTChanged = true;
  							break;
  						}
  					}
  				}
  			}
  		}

  		if (futAdvTTChanged){
  			ArrayList projAdvCostCurveIdList = new ArrayList();
  			projAdvCostCurveIdList.add(CostCurveService.CODE_NEW_TREATMENT_PLANT);
  			projAdvCostCurveIdList.add(CostCurveService.CODE_INCREASE_FLOW_CAPACITY);
  			projAdvCostCurveIdList.add(CostCurveService.CODE_INCREASE_LEVEL_TREATMENT);
  			projAdvCostCurveIdList.add(CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT);
  			projAdvCostCurveIdList.add(CostCurveService.CODE_REPLACE_TREATMENT_PLANT);
  			dischargeService.setUpCostCurveCodesForRerun(projAdvCostCurveIdList, facilityIdString);
  		}
  	}
	
	//checks if a given treatment type indicator is still retained
	private boolean isTTIndicatorRetained(String ttIndicator, String[] advanceTreatmentIndicators){
		boolean isRetained = false;
		if (advanceTreatmentIndicators!=null && advanceTreatmentIndicators.length>0){
			for(int i=0; i< advanceTreatmentIndicators.length; i++){
				if (advanceTreatmentIndicators[i].equals(ttIndicator)){
					isRetained = true;
					break;
				}
			}
		}
		return isRetained;
	}
	
	//is stateView
	private boolean isStateView(CurrentUser currentUser){
		boolean isStateView = false; 
		isStateView = currentUser.getCurrentRole().getLocationTypeId().equals(UserService.LOCATION_TYPE_ID_LOCAL)?false:true;
		return isStateView;
	}
	
	public String[] getEffluentIds(Collection advanceTreatmentTypes){
		ArrayList arr = new ArrayList();
		for (Iterator iter = advanceTreatmentTypes.iterator(); iter.hasNext();) {
			FacilityAdvancedTreatment fat = (FacilityAdvancedTreatment) iter.next();			
			arr.add(fat.getId().getAdvancedTreatmentTypeId()+"");
		}
		return (String[])arr.toArray();		
	}

    private FESManager fesManager;
    public void setFesManager(FESManager fesm){
    	fesManager=fesm;
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
	
	private DischargeService dischargeService;
	public void setDischargeService(DischargeService dischargeService) {
		this.dischargeService = dischargeService;
	}
}


